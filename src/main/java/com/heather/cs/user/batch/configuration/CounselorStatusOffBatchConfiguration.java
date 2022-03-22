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
public class CounselorStatusOffBatchConfiguration implements Job {

	public static final String JOB_NAME = "counselorStatusOffJob";
	public static final String COUNSELOR_OFF_STEP = "counselorStatusOffStep";
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final UserMapper userMapper;

	@Bean
	public Job counselorStatusOffJob() {
		return jobBuilderFactory.get(JOB_NAME)
			.start(counselorStatusOffStep())
			.build();
	}

	@Bean
	public Step counselorStatusOffStep() {
		return stepBuilderFactory.get(COUNSELOR_OFF_STEP)
			.tasklet((contribution, chunkContext) -> {
				for(User x : userMapper.selectStatusOnUser()) {
					System.out.println(x.getId());
					userMapper.updateUserStatusToOff(x.getId());
					userMapper.insertUserHistory(x.getId());
				}
				return RepeatStatus.FINISHED;
			})
			.allowStartIfComplete(true)
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
