package com.heather.cs.user.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.heather.cs.batch.CustomJob;
import com.heather.cs.configuration.DatabaseConfiguration;
import com.heather.cs.user.batch.jobparameters.CounselorOffJobParametersValidator;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Import(DatabaseConfiguration.class)
public class CounselorTestBatchConfiguration implements CustomJob {

	public static final String JOB_NAME = "counselorOffJob";
	public static final String STEP_NAME = "counselorStatusOffStep";
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final UserMapper userMapper;

	@Override
	@Bean(JOB_NAME)
	public Job job() {
		return jobBuilderFactory.get(JOB_NAME)
			.start(step())
			.build();
	}

	@Override
	@Bean(STEP_NAME)
	public Step step() {
		return stepBuilderFactory.get(STEP_NAME)
			.tasklet((contribution, chunkContext) -> {
				for(User x : userMapper.selectStatusOnUser()) {
					userMapper.updateUserStatusToOff(x.getId());
					userMapper.insertUserHistory(x.getId());
				}
				return RepeatStatus.FINISHED;
			})
			.allowStartIfComplete(true)
			.build();
	}

	@Override
	public String getJobName() {
		return JOB_NAME;
	}
}
