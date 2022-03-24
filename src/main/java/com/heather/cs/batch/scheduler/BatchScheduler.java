package com.heather.cs.batch.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {

	// private final JobLauncher jobLauncher;
	// private final CounselorStatusOffBatchConfiguration counselorBatchConfiguration;
	// private final DelayedCounselBatchConfiguration delayedCounselBatchConfiguration;
	//
	// @Scheduled(cron = "0 0 19 * * *")
	// public void runChangeUserStatusJob() {
	// 	try {
	// 		jobLauncher.run(counselorBatchConfiguration.counselorStatusOffJob(), new JobParameters());
	// 	} catch (Exception exception) {
	// 		log.error("Fail to run changeUserStatusJob", exception);
	// 	}
	// }
	//
	// @Scheduled(cron = "0 0 */1 * * *")
	// public void runChangeCategoryJob() {
	// 	try {
	// 		jobLauncher.run(delayedCounselBatchConfiguration.changeCategoryJob(), new JobParameters());
	// 	} catch (Exception exception) {
	// 		log.error("Fail to run changeCategoryJob", exception);
	// 	}
	// }
}
