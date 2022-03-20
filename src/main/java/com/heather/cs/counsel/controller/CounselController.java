package com.heather.cs.counsel.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.configuration.annotation.LogInUser;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.service.CounselService;
import com.heather.cs.response.Response;
import com.heather.cs.response.code.ResponseCode;
import com.heather.cs.response.message.ResponseMessage;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselController {

	private final CounselService counselService;
	private static final Response success = new Response();

	@PostMapping("/counsel")
	public Response registerCounsel(@Valid @RequestBody Counsel counsel) {
		counselService.registerCounsel(counsel);
		return success;
	}

	@GetMapping("/counsels/assignment")
	public Response assignCounsels(@LogInUser User user) {
		counselService.assignCounsels(user.getId());
		return success;
	}

	@GetMapping("/counsels")
	public Response countCounselsWithoutCharger(@LogInUser User user) {
		int numberOfCounsels = counselService.countCounselsWithoutCharger(user.getId());
		if (numberOfCounsels == 0) {
			return new Response<>(ResponseCode.SUCCESS, ResponseMessage.ALL_ASSIGNED);
		}
		return new Response<>(numberOfCounsels);
	}

}
