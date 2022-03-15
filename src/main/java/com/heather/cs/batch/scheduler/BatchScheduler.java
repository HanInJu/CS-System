package com.heather.cs.batch.scheduler;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.heather.cs.counsel.batch.configuration.DelayedCounselBatchConfiguration;
import com.heather.cs.user.batch.configuration.CounselorBatchConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {

	private final JobLauncher jobLauncher;
	private final CounselorBatchConfiguration counselorBatchConfiguration;
	private final DelayedCounselBatchConfiguration delayedCounselBatchConfiguration;

	@Scheduled(cron = "0 0 19 * * *")
	public void runChangeUserStatusJob() {
		try {
			jobLauncher.run(counselorBatchConfiguration.counselorStatusOffJob(), new JobParameters());
			// 리턴이 Execution
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
