package com.heather.cs.batch.scheduler;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.heather.cs.counsel.batch.configuration.TestBatchConfiguration;
import com.heather.cs.user.batch.configuration.CounselorStatusOffBatchConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {

	private final JobLauncher jobLauncher;
	private final CounselorStatusOffBatchConfiguration counselorBatchConfiguration;
	private final TestBatchConfiguration delayedCounselBatchConfiguration;

	@Scheduled(cron = "0 0 19 * * *")
	public void runChangeUserStatusJob() {
		try {
			jobLauncher.run(counselorBatchConfiguration.counselorStatusOffJob(), new JobParameters());
		} catch (Exception exception) {
			log.error(exception.getMessage());
		}
	}

	@Scheduled(cron = "0 0 */1 * * *")
	public void runChangeCategoryJob() {
		try {
			jobLauncher.run(delayedCounselBatchConfiguration.changeCategoryJob(), new JobParameters());
		} catch (Exception exception) {
			log.error(exception.getMessage());
		}
	}
}
