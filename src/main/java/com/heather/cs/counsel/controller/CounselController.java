package com.heather.cs.counsel.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@PostMapping("/counsel")
	public ResponseEntity<Response> registerCounsel(@Valid @RequestBody Counsel counsel) {
		counselService.registerCounsel(counsel);
		return new ResponseEntity<>(new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS), HttpStatus.OK);
	}

	@GetMapping("/counsels/assignment")
	public ResponseEntity<Response> assignCounsels(@LogInUser User user) {
		counselService.assignCounsels(user.getId());
		return new ResponseEntity<>(new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS), HttpStatus.OK);
	}

	@GetMapping("/counsels")
	public ResponseEntity<Response<Integer>> countCounselsWithoutCharger(@LogInUser User user) {
		int numberOfCounsels = counselService.countCounselsWithoutCharger(user.getId());
		if (numberOfCounsels == 0) {
			return new ResponseEntity<Response<Integer>>(
				new Response(ResponseCode.SUCCESS, "All counsels are assigned."), HttpStatus.OK);
		}
		return new ResponseEntity<Response<Integer>>(
			new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, numberOfCounsels), HttpStatus.OK);
	}

}
