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
public class DelayedCounselBatchConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	// private final JobCompletionNotificationListener listener;
	private final DataSource dataSource;

	private static final String MODIFIED_BY_SYSTEM = "SYSTEM";
	private static final String DELAYED_COUNSEL_STATUS = "UNASSIGNED";

	private final Long NEW_CATEGORY_ID = 23L; // categoryMapper.selectCategoryId(DELAYED_COUNSEL_CATEGORY)

	private static final String JOB_NAME = "changeCategoryJob";
	private static final String STEP_NAME = "changeCategoryStep";
	private static final int chunkSize = 10;

	@Bean
	public Job changeCategoryJob() throws Exception { //.listener(listener) .incrementer(new RunIdIncrementer())
		return jobBuilderFactory.get(JOB_NAME)
			.start(changeCategoryStep())
			.build();
	}

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
		queryProvider.setWhereClause("WHERE status = :status AND DATEDIFF(DATE(NOW()), DATE(created_at)) > 2");

		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.ASCENDING);
		queryProvider.setSortKeys(sortKeys);

		return queryProvider.getObject();
	}

	@Bean
	public ItemProcessor<Counsel, Counsel> changeCategoryProcessor() {
		return counsel -> {
			counsel.setCategoryId(NEW_CATEGORY_ID);
			counsel.setModifierId(MODIFIED_BY_SYSTEM);
			log.info("COUNSEL ID : " + counsel.getId());
			return counsel;
		};
	}

	@Bean
	public CompositeItemWriter<Counsel> compositeCounselItemWriter() {
		CompositeItemWriter<Counsel> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(updateCounselWriter(), insertCounselHistoryWriter()));
		return writer;
	}

	@Bean
	public JdbcBatchItemWriter<Counsel> updateCounselWriter() {
		return new JdbcBatchItemWriterBuilder<Counsel>()
			.dataSource(dataSource)
			.sql("UPDATE counsel SET category_id = :categoryId, modifier_id = :modifierId, modified_at = NOW() WHERE id = :id")
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
}
