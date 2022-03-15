package com.heather.cs.user.batch.configuration;

import java.util.Arrays;
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
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.collect.Maps;
import com.heather.cs.code.CounselorStatus;
import com.heather.cs.configuration.DatabaseConfiguration;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Import(DatabaseConfiguration.class)
public class CounselorBatchConfiguration {

	public static final String JOB_NAME = "counselorStatusOffJob";

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	private final int chunkSize = 10; // 만 단위까지는 쿼리로 처리하는데 상담원은 만 명보다 적어서 보통 이런 건 tasklet으로 처리한다.
	//tasklet으로 User 테이블 update 후 for로 history insert하면 문제 없어보임

	//tasklet, chunk : transaction 단위가 다르지
	//그래서 이번에 tasklet으로 하는 게 더 나음(transaction) : 상담원이 많지 않으니까!

	@Bean
	public Job counselorStatusOffJob() throws Exception {
		return jobBuilderFactory.get(JOB_NAME)
			.start(counselorStatusOffStep())
			.build();
	}

	@Bean
	public Step counselorStatusOffStep() throws Exception {
		return stepBuilderFactory.get("counselorStatusOffStep")
			.<User, User>chunk(chunkSize)
			.reader(counselorStatusOffReader())
			.processor(counselorStatusOffProcessor())
			.writer(compositeItemWriter())
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	public JdbcPagingItemReader<User> counselorStatusOffReader() throws Exception {
		Map<String, Object> parameterMap = Maps.newHashMap();
		parameterMap.put("status", CounselorStatus.AVAILABLE.toString());
		parameterMap.put("use_yn", "Y");

		//mybatis mapper로 페이징처리해서 read하고 write도 mybatis로 처리하는 편임!
		//쿼리를 String으로 만들면 실수하기 쉬우니까

		return new JdbcPagingItemReaderBuilder<User>()
			.pageSize(chunkSize)
			.fetchSize(chunkSize)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(User.class))
			.queryProvider(queryProvider())
			.parameterValues(parameterMap)
			.name("counselorStatusOffItemReader")
			.build();
	}

	@Bean
	public PagingQueryProvider queryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
		queryProviderFactoryBean.setDataSource(dataSource);
		queryProviderFactoryBean.setSelectClause("id, role, status, use_yn");
		queryProviderFactoryBean.setFromClause("FROM user");
		queryProviderFactoryBean.setWhereClause("WHERE status = :status AND use_yn = :use_yn");

		Map<String, Order> sortKeys = Maps.newHashMap();
		sortKeys.put("id", Order.ASCENDING);
		queryProviderFactoryBean.setSortKeys(sortKeys);

		return queryProviderFactoryBean.getObject();
	}

	@Bean
	public ItemProcessor<User, User> counselorStatusOffProcessor() { // 그냥 쿼리로 처리하는 게 나았을 수도?
		// 동적쿼리 만들 때 IN 절 조건 개수 찾아보고
		return user -> {
			user.setStatus(CounselorStatus.UNAVAILABLE.toString());
			user.setModifierId("SYSTEM");
			return user;
		};
	}

	@Bean
	public CompositeItemWriter<User> compositeItemWriter() {
		CompositeItemWriter<User> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(counselorStatusOffWriter(), counselorStatusOffHistoryWriter()));
		return writer;
	}

	@Bean
	public JdbcBatchItemWriter<User> counselorStatusOffWriter() {
		return new JdbcBatchItemWriterBuilder<User>()
			.dataSource(dataSource)
			.sql("UPDATE user SET status = :status, modifier_id = 'SYSTEM', modified_at = NOW() WHERE id = :id")
			.beanMapped()
			.build();
	}

	@Bean
	public JdbcBatchItemWriter<User> counselorStatusOffHistoryWriter() {
		return new JdbcBatchItemWriterBuilder<User>()
			.dataSource(dataSource)
			.sql("INSERT INTO user_history (user_id, sequence, name, password, role, status, use_yn, creator_id, created_at)\n"
				+ "SELECT u.id as user_id,\n"
				+ "       (COUNT(uh.user_id)) + 1 AS sequence,\n"
				+ "       u.name, u.password, u.role, u.status, u.use_yn, modifier_id, modified_at\n"
				+ "FROM user AS u\n"
				+ "LEFT JOIN user_history uh on u.id = uh.user_id\n"
				+ "WHERE u.id = :id\n"
				+ "GROUP BY u.id")
			.beanMapped()
			.build();
	}

}
