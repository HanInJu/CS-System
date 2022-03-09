package com.heather.cs.user.batch.configuration;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.annotation.Scheduled;

import com.google.common.collect.Maps;
import com.heather.cs.code.CounselorStatus;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CounselorStatusOffJobConfiguration {

	public static final String JOB_NAME = "counselorStatusOffJob";

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	private final int chunkSize = 10;

	@Bean
	@Scheduled(cron =  "0 0 19 * * ?")
	public Job jdbcPagingItemReaderJob() throws Exception {
		return jobBuilderFactory.get(JOB_NAME)
			.start(jdbcPagingItemReaderStep())
			.build();
	}

	@Bean
	public Step jdbcPagingItemReaderStep() throws Exception {
		return stepBuilderFactory.get("counselorStatusOffStep")
			.<User, User>chunk(chunkSize)
			.reader(jdbcPagingItemReader())
			.writer(jdbcPagingItemWriter())
			.build();
	}

	@Bean
	public JdbcPagingItemReader<User> jdbcPagingItemReader() throws Exception {
		Map<String, Object> parameterMap = Maps.newHashMap();
		parameterMap.put("status", CounselorStatus.AVAILABLE.toString());
		parameterMap.put("use_yn", "Y");

		return new JdbcPagingItemReaderBuilder<User>()
			.pageSize(chunkSize)
			.fetchSize(chunkSize)
			.dataSource(dataSource)
			.rowMapper(new BeanPropertyRowMapper<>(User.class))
			.queryProvider(createQueryProvider())
			.parameterValues(parameterMap)
			.name("counselorStatusOffItemReader")
			.build();
	}

	@Bean
	public JdbcBatchItemWriter<User> jdbcPagingItemWriter() {
		return new JdbcBatchItemWriterBuilder<User>()
			.dataSource(dataSource)
			.sql("UPDATE user SET status = :status, modifier_id = :id, modified_at = NOW() WHERE id = :id")
			.beanMapped()
			.build();
	}

	@Bean
	public PagingQueryProvider createQueryProvider() throws Exception {
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
}
