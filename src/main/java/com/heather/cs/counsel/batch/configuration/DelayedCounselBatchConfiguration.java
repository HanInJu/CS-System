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
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
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

import com.heather.cs.configuration.DatabaseConfiguration;
import com.heather.cs.counsel.batch.jobparameters.MoveCounselCategoryJobParameters;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.user.batch.jobparameters.CounselorOffJobParametersValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Import(DatabaseConfiguration.class)
public class DelayedCounselBatchConfiguration implements Job {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final SqlSessionFactory sqlSessionFactory;
	private final MoveCounselCategoryJobParameters jobParameters;

	private static final String JOB_NAME = "changeCategoryJob";
	private static final String STEP_NAME = "changeCategoryStep";
	private static final int chunkSize = 1000;

	@Bean
	public Job changeCategoryJob() throws Exception {
		return jobBuilderFactory.get(JOB_NAME)
			.start(changeCategoryStep())
			.build();
	}

	@Bean(JOB_NAME + "_jobParameter")
	@JobScope
	public MoveCounselCategoryJobParameters jobParameter() {
		return new MoveCounselCategoryJobParameters();
	}

	@Bean
	@JobScope
	public Step changeCategoryStep() throws Exception {
		return stepBuilderFactory.get(STEP_NAME)
			.<Counsel, Counsel>chunk(chunkSize)
			.reader(delayedCounselReader())
			.writer(compositeCounselItemWriter())
			.allowStartIfComplete(true)
			.build();
	}

	@Bean
	@StepScope
	public MyBatisPagingItemReader<Counsel> delayedCounselReader() {
		Map<String, Object> params = new HashMap<>();
		params.put("date", jobParameters.getDate());
		log.info(">>>>>>>>>>> date = {}", jobParameters.getDate());

		return new MyBatisPagingItemReaderBuilder<Counsel>()
			.sqlSessionFactory(sqlSessionFactory)
			.queryId("com.heather.cs.counsel.mapper.CounselMapper.selectDelayedCounsel")
			.parameterValues(params)
			.build();
	}

	@Bean
	public CompositeItemWriter<Counsel> compositeCounselItemWriter() {
		CompositeItemWriter<Counsel> writer = new CompositeItemWriter<>();
		writer.setDelegates(Arrays.asList(delayedCounselWriter(), delayedCounselInHistoryWriter()));
		return writer;
	}

	@Bean
	@StepScope
	public MyBatisBatchItemWriter<Counsel> delayedCounselWriter() {
		return new MyBatisBatchItemWriterBuilder<Counsel>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.heather.cs.counsel.mapper.CounselMapper.updateCounselCategory")
			.itemToParameterConverter(createItemToParameterMapConverter())
			.build();
	}

	@Bean
	@StepScope
	public MyBatisBatchItemWriter<Counsel> delayedCounselInHistoryWriter() {
		return new MyBatisBatchItemWriterBuilder<Counsel>()
			.sqlSessionFactory(sqlSessionFactory)
			.statementId("com.heather.cs.counsel.mapper.CounselMapper.insertCounselHistoryInBatch")
			.itemToParameterConverter(createItemToParameterMapConverter())
			.build();
	}

	@Override
	public String getName() {
		return JOB_NAME;
	}

	@Override
	public boolean isRestartable() {
		return true;
	}

	@Override
	public void execute(JobExecution jobExecution) {

	}

	@Override
	public JobParametersIncrementer getJobParametersIncrementer() {
		return null;
	}

	@Override
	public JobParametersValidator getJobParametersValidator() {
		return new CounselorOffJobParametersValidator();
	}
}
