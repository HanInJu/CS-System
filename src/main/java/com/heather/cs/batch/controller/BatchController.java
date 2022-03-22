package com.heather.cs.batch.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.response.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchController {

	private final JobLauncher jobLauncher;
	// private final CounselorStatusOffBatchConfiguration counselorStatusOffBatch;
	// private final DelayedCounselBatchConfiguration delayedCounselBatch;
	private final Job[] jobs;
	private static final Response success = new Response();

	//배치는 하나하나 처리하지 않고 jobName을 받아서 처리한다.
	//배치가 실행될 조건이 있는데, 수동으로 배치를 실행하려고 할 때는 그 날에 대한 정보가 들어갈 수 있어야 한다.
	//보통 start, end 기간를 엄격하게 처리하는 편 (범위를 주지 않으면 select가 많아짐)

	// @PatchMapping("/batch/user/status/off")
	// public Response runChangeUserStatusJob() throws Exception {
	// 	jobLauncher.run(counselorStatusOffBatch.counselorStatusOffJob(), new JobParameters());
	// 	return success;
	// }
	//
	// @PatchMapping("/batch/counsel/delay")
	// public Response runMoveCategoryJob(@RequestBody MoveCounselCategoryJobParameters params) throws Exception {
	// 	JobParameters jobParameters = new JobParametersBuilder()
	// 		.addString("date", params.getDate().toString())
	// 		.toJobParameters();
	//
	// 	jobLauncher.run(delayedCounselBatch.changeCategoryJob(), jobParameters);
	// 	return success;
	// }

	@GetMapping("/batch")
	public Response test(@RequestBody Map<String, String> map) { // "date" : "2022-03-22"

		Job job = getJob(map);
		JobParameters jobParameters = setJobParameters(map);

		try {
			jobLauncher.run(job, jobParameters);
		}
		catch (Exception exception) {
			log.error("Fail to run by jobName", exception);
		}

		return success;
	}

	public Job getJob(Map<String, String> map) {
		if (!map.containsKey("jobName")) {
			throw new IllegalArgumentException("jobName is NOT entered");
		}
		String jobName = map.get("jobName");
		Optional<Job> optionalJob = Arrays.stream(jobs)
			.filter(x -> x.getName().equals(jobName))
			.findFirst();

		return optionalJob.orElseThrow(
			() -> new IllegalArgumentException("Invalid jobName (jobName : " + jobName + ")"));
	}

	public JobParameters setJobParameters(Map<String, String> map) {
		if (!map.containsKey("date")) {
			map.put("date", LocalDate.now().toString());
		}
		return new JobParametersBuilder()
			.addString("date", map.get("date"))
			.toJobParameters();
	}

}
