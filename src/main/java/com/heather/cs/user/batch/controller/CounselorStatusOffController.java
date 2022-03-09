package com.heather.cs.user.batch.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.user.batch.configuration.CounselorStatusOffJobConfiguration;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselorStatusOffController {

	public final CounselorStatusOffJobConfiguration jobConfiguration;

	@PatchMapping("/job")
	public void changeStatusOff(@RequestParam(name = "name") String jobName) throws Exception {
		if(jobName.equals("counselorStatusOffJob")) {
			jobConfiguration.jdbcPagingItemReaderJob();
		}
	}
}
