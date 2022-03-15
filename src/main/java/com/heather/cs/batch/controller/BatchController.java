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

	//배치는 하나하나 처리하지 않고 jobName을 받아서 처리한다.
	//배치가 실행될 조건이 있는데, 수동으로 배치를 실행하려고 할 때는 그 날에 대한 정보가 들어갈 수 있어야 한다.
	//보통 start, end 기간를 엄격하게 처리하는 편 (범위를 주지 않으면 select가 많아짐)

	@PatchMapping("/batch/user/status/off")
	public Response runChangeUserStatusJob() throws Exception {
		jobLauncher.run(counselorBatchConfiguration.counselorStatusOffJob(), new JobParameters());
		return response.successResponse(); // static 객체로 만들어서 쓰는 게 나음 보통 쓰는 컨트롤ㄹ러마다 하나 만들어도 됨
	}

	@PatchMapping("/batch/counsel/delay")
	public Response runChangeCategoryJob() throws Exception {
		jobLauncher.run(delayedCounselBatchConfiguration.changeCategoryJob(), new JobParameters());
		return response.successResponse();
	}

}
