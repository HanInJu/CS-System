package com.heather.cs.counsel.batch.configuration;

import static com.heather.cs.converter.ItemToParameterMapConverters.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.heather.cs.batch.CustomJob;
import com.heather.cs.configuration.DatabaseConfiguration;
import com.heather.cs.counsel.batch.jobparameters.MoveCounselCategoryJobParameters;
import com.heather.cs.counsel.dto.Counsel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Import(DatabaseConfiguration.class)
public class DelayedCounselBatchConfiguration implements CustomJob {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;
	private final MoveCounselCategoryJobParameters jobParameters;

	private static final String JOB_NAME = "changeCategoryJob";
	private static final String STEP_NAME = "changeCategoryStep";
	private static final int chunkSize = 1000;

	@Bean(JOB_NAME)
	@Override
	public Job job() {
		return jobBuilderFactory.get(JOB_NAME)
			.start(step())
			.build();
	}

	@Bean(JOB_NAME + "_jobParameter")
	@JobScope
	public MoveCounselCategoryJobParameters jobParameter() {
		return new MoveCounselCategoryJobParameters();
	}


	@Override
	@Bean(STEP_NAME)
	@JobScope
	public Step step() {
		return stepBuilderFactory.get(STEP_NAME)
			.<Counsel, Counsel>chunk(chunkSize)
			.reader(changeCategoryItemReader())
			.writer(changeCategoryCompositeItemWriter())
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	@StepScope
	public MyBatisPagingItemReader<Counsel> changeCategoryItemReader() {
		Map<String, Object> params = new HashMap<>();
		params.put("date", jobParameters.getDate());

		return new MyBatisPagingItemReaderBuilder<Counsel>()
			.sqlSessionFactory(sqlSessionFactory)
			.queryId("com.heather.cs.counsel.mapper.CounselMapper.selectDelayedCounsel")
			.parameterValues(params)
			.build();
	}

	@Bean
	public CompositeItemWriter<Counsel> changeCategoryCompositeItemWriter() {
		CompositeItemWriter<Counsel> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(updateCounselCategoryWriter(), insertCounselCategoryInHistoryWriter()));
		return writer;
	}

	@Bean
	@StepScope
	public MyBatisBatchItemWriter<Counsel> updateCounselCategoryWriter() {
		return new MyBatisBatchItemWriterBuilder<Counsel>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.heather.cs.counsel.mapper.CounselMapper.updateCounselCategory")
			.itemToParameterConverter(createItemToParameterMapConverter())
			.build();
	}

	@Bean
	@StepScope
	public MyBatisBatchItemWriter<Counsel> insertCounselCategoryInHistoryWriter() {
		return new MyBatisBatchItemWriterBuilder<Counsel>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.heather.cs.counsel.mapper.CounselMapper.insertCounselHistoryInBatch")
			.itemToParameterConverter(createItemToParameterMapConverter())
			.build();
	}

	@Override
	public String getJobName() {
		return JOB_NAME;
	}
}
