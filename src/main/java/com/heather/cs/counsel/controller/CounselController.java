package com.heather.cs.counsel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.annotation.LogInUser;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.service.CounselService;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselController {

	private final CounselService counselService;
	private final UserService userService;

	@PostMapping("/counsel")
	public void registerCounsel(@RequestBody Counsel counsel) {
		counselService.registerCounsel(counsel);
	}

	@GetMapping("/counsels/assignment")
	public void assignCounsels(@LogInUser User user) {
		if(userService.hasManagerPrivileges(user.getId())) {
			throw new IllegalArgumentException("No Permission : userId = " + user.getId());
		}
		counselService.assignCounsels(user.getId());
	}

	@GetMapping("/counsels")
	public int countCounselsWithoutCharger(@LogInUser User user) {
		if(userService.hasManagerPrivileges(user.getId())) {
			throw new IllegalArgumentException("No Permission : userId = " + user.getId());
		}
		return counselService.countCounselsWithoutCharger(user.getId());
	}
}
