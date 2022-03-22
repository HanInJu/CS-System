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

import com.heather.cs.batch.CustomJob;
import com.heather.cs.response.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BatchController {

	private final JobLauncher jobLauncher;
	private final CustomJob[] jobs;
	private static final Response success = new Response();

	@GetMapping("/batch")
	public Response runBatchByJobName(@RequestBody Map<String, String> map) {

		CustomJob job = getJob(map);
		JobParameters jobParameters = setJobParameters(map);

		try {
			jobLauncher.run(job.job(), jobParameters);
		}
		catch (Exception exception) {
			log.error("Fail to run by jobName", exception);
		}

		return success;
	}

	public CustomJob getJob(Map<String, String> map) {
		if (!map.containsKey("jobName")) {
			throw new IllegalArgumentException("jobName is NOT entered");
		}
		String jobName = map.get("jobName");
		Optional<CustomJob> optionalJob = Arrays.stream(jobs)
			.filter(x -> x.getJobName().equals(jobName))
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
