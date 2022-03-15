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
import com.heather.cs.response.message.ResponseMessage;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselController {

	private final CounselService counselService;
	private final Response response;

	@PostMapping("/counsel")
	public Response registerCounsel(@Valid @RequestBody Counsel counsel) {
		counselService.registerCounsel(counsel);
		return response.successResponse();
	}

	@GetMapping("/counsels/assignment")
	public Response assignCounsels(@LogInUser User user) {
		counselService.assignCounsels(user.getId());
		return response.successResponse();
	}

	@GetMapping("/counsels")
	public Response countCounselsWithoutCharger(@LogInUser User user) {
		int numberOfCounsels = counselService.countCounselsWithoutCharger(user.getId());
		if (numberOfCounsels == 0) {
			return response.messageResponse(ResponseMessage.ALL_ASSIGNED);
		}
		return response.withData(numberOfCounsels);
	}

}
