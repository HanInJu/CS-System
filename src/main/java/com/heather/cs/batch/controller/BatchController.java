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
	private final CustomJob[] jobs; // 이렇게 말고 다른 방법 생각해보기 (configuration이라는 이름X) customJob은 너무 범용 이름임
	private static final Response success = new Response();

	@GetMapping("/batch")
	public Response runBatchByJobName(@RequestBody Map<String, String> map) { //쿼리스트링으로 받는 게 나음
		// 포스트맨을 쓰기가 쉽지 않은 경우도 있음(막아버리는 경우) -> 그러면 바디 쓰기가 곤란한 경우도 있으니까

		CustomJob job = getJob(map);
		JobParameters jobParameters = setJobParameters(map);

		try {
			jobLauncher.run(job.job(), jobParameters);
		}
		catch (Exception exception) {
			log.error("Fail to run by jobName", exception); // 이러면 항상 success라서 처리할 방법 다시 생각
		}

		return success;
	}

	public CustomJob getJob(Map<String, String> map) {
		if (!map.containsKey("jobName")) {
			throw new IllegalArgumentException("jobName is NOT entered"); // 예외처리
		}
		String jobName = map.get("jobName");
		Optional<CustomJob> optionalJob = Arrays.stream(jobs)
			.filter(x -> x.getJobName().equals(jobName))
			.findFirst();

		return optionalJob.orElseThrow(
			() -> new IllegalArgumentException("Invalid jobName (jobName : " + jobName + ")")); // 위에서 체크했는데 또 함
	}

	public JobParameters setJobParameters(Map<String, String> map) { // 개별 잡에 대한 파라미터여야지 각가의 잡에 맞는 파라미터를 세팅할 수 있음
		// 그냥 map에 있는 걸 다 읽어서 잡 파라미터로
		if (!map.containsKey("date")) {
			map.put("date", LocalDate.now().toString());
		}
		return new JobParametersBuilder()
			.addString("date", map.get("date"))
			.toJobParameters();
	}

}
