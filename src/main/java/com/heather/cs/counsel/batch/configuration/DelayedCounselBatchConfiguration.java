package com.heather.cs.counsel.batch.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.heather.cs.configuration.DatabaseConfiguration;
import com.heather.cs.counsel.dto.Counsel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Import(DatabaseConfiguration.class)
public class DelayedCounselBatchConfiguration implements ItemReader {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	private static final String MODIFIED_BY_SYSTEM = "SYSTEM";
	private static final String DELAYED_COUNSEL_STATUS = "UNASSIGNED";

	private final Long NEW_CATEGORY_ID = 23L; // 보통 예외 카테고리를 두는 편이라 쿼리로 id 가져오지 않아도

	private static final String JOB_NAME = "changeCategoryJob";
	private static final String STEP_NAME = "changeCategoryStep";
	private static final int chunkSize = 10; // 단위가 너무 작으면 배치가 오래 돌지... 1000 단위로

	@Bean
	public Job changeCategoryJob() throws Exception {
		return jobBuilderFactory.get(JOB_NAME)
			.start(changeCategoryStep())
			.build();
	}

	// 1. 파라미터 세팅
	// 2. reader-processor-writer

	@Bean
	public Step changeCategoryStep() throws Exception {
		return stepBuilderFactory.get(STEP_NAME)
			.<Counsel, Counsel>chunk(chunkSize)
			.reader(changeCategoryReader())
			.processor(changeCategoryProcessor())
			.writer(compositeCounselItemWriter())
			.allowStartIfComplete(true)
			.build();
	}

	// fetchSize
	// 읽어오는 단위, 쓰는 단위 : 쓰는 단위를 더 중요하게 여김(트랜잭션) -> 쓰는 단위를 chunk size로 많이 설정한다.

	// 보i

	@Bean
	public JdbcPagingItemReader<Counsel> changeCategoryReader() throws Exception {
		Map<String, Object> parameterValues = new HashMap<>();
		parameterValues.put("status", DELAYED_COUNSEL_STATUS);

		return new JdbcPagingItemReaderBuilder<Counsel>()
			.pageSize(chunkSize)
			.fetchSize(chunkSize)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(Counsel.class))
			.queryProvider(selectQueryProvider())
			.parameterValues(parameterValues)
			.name("changeCategoryReader")
			.build();
	}

	@Bean
	public PagingQueryProvider selectQueryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
		queryProvider.setDataSource(dataSource);
		queryProvider.setSelectClause("id, category_id, modifier_id, modified_at");
		queryProvider.setFromClause("FROM counsel");
		queryProvider.setWhereClause("WHERE status = :status AND DATEDIFF(DATE(NOW()), DATE(created_at)) > 2"); // 잘못된 쿼리임
		// 좌변은 가공하지 말아야 하는데, 좌변을 가공하면 index를 탈 수가 없음! DATEDIFF X
		// 보통 create_at이 index인데 이렇게 가공하면 인덱스 안 탐
		// 항상 우변을 가공하기
		// created_at 날짜차이 : 어디까지 저장되는지 -> 우변을 가공함

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);

		return queryProvider.getObject();
	}

	@Bean
	public ItemProcessor<Counsel, Counsel> changeCategoryProcessor() { // 프로세서는 많이 생략되는 편
		//다 같은 값으로 바꿔서 WRITE를 하는 거면 의미가 없어진다.
		return counsel -> {
			counsel.setCategoryId(NEW_CATEGORY_ID);
			counsel.setModifierId(MODIFIED_BY_SYSTEM);
			return counsel;
		};
	}

	// chunk는 writer가 한 트랜잭션의 단위...
	@Bean
	public CompositeItemWriter<Counsel> compositeCounselItemWriter() {
		CompositeItemWriter<Counsel> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(updateCounselWriter(), insertCounselHistoryWriter()));
		return writer;
	}

	// 보통 JdbcBatchItemWriter 말고 ItemWriter를 만들어서 쓴다. MyBatis로 하면 쿼리를 안 써도
	@Bean
	public JdbcBatchItemWriter<Counsel> updateCounselWriter() { // 여기서 Counsel 왜 안받는데 어떻게 실행이 됐지???
		return new JdbcBatchItemWriterBuilder<Counsel>()
			.dataSource(dataSource)
			.sql(
				"UPDATE counsel SET category_id = :categoryId, modifier_id = :modifierId, modified_at = NOW() WHERE id = :id")
			.beanMapped()
			.build();
	}

	@Bean
	public JdbcBatchItemWriter<Counsel> insertCounselHistoryWriter() {
		return new JdbcBatchItemWriterBuilder<Counsel>()
			.dataSource(dataSource)
			.sql(
				"INSERT INTO counsel_history(counsel_id, sequence, category_id, title, content, requestor_email, charger_id, status, creator_id, created_at)\n"
					+ "SELECT c.id                       AS counsel_id,\n"
					+ "       (COUNT(ch.counsel_id)) + 1 AS sequence,\n"
					+ "       c.category_id,\n"
					+ "       c.title,\n"
					+ "       c.content,\n"
					+ "       customer_email             AS requestor_email,\n"
					+ "       c.charger_id,\n"
					+ "       c.status,\n"
					+ "       modifier_id,\n"
					+ "       modified_at\n"
					+ "FROM counsel AS c\n"
					+ "LEFT JOIN counsel_history AS ch on c.id = ch.counsel_id\n"
					+ "WHERE c.id = :id")
			.beanMapped()
			.build();
	}

	@Override
	public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return null;
	}
}
