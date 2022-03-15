package com.heather.cs.batch.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.counsel.batch.configuration.DelayedCounselBatchConfiguration;
import com.heather.cs.response.Response;
import com.heather.cs.user.batch.configuration.CounselorBatchConfiguration;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BatchController {

	private final JobLauncher jobLauncher;
	private final CounselorBatchConfiguration counselorBatchConfiguration;
	private final DelayedCounselBatchConfiguration delayedCounselBatchConfiguration;
	private final Response response;

	@PatchMapping("/batch/user/status/off")
	public Response runChangeUserStatusJob() throws Exception {
		jobLauncher.run(counselorBatchConfiguration.counselorStatusOffJob(), new JobParameters());
		return response.successResponse();
	}

	@PatchMapping("/batch/counsel/delay")
	public Response runChangeCategoryJob() throws Exception {
		jobLauncher.run(delayedCounselBatchConfiguration.changeCategoryJob(), new JobParameters());
		return response.successResponse();
	}

}
