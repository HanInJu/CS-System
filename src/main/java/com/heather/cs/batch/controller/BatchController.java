package com.heather.cs.batch.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.counsel.batch.configuration.DelayedCounselBatchConfiguration;
import com.heather.cs.user.batch.configuration.CounselorBatchConfiguration;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BatchController {

	private final JobLauncher jobLauncher;
	private final CounselorBatchConfiguration counselorBatchConfiguration;
	private final DelayedCounselBatchConfiguration delayedCounselBatchConfiguration;

	@PatchMapping("/batch/user/status/off")
	public void runChangeUserStatusJob() throws Exception {
		jobLauncher.run(counselorBatchConfiguration.counselorStatusOffJob(), new JobParameters());
	}

	@PatchMapping("/batch/counsel/delay")
	public void runChangeCategoryJob() throws Exception {
		jobLauncher.run(delayedCounselBatchConfiguration.changeCategoryJob(), new JobParameters());
	}

}
