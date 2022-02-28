package com.heather.cs.counsel.controller;

import javax.servlet.http.Cookie;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.service.CounselService;
import com.heather.cs.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselController {

	private final CounselService counselService;
	private final UserService userService;
	private static final String LOGIN_COOKIE = "userIdCookie";

	@PostMapping("/counsel")
	public void registerCounsel(@RequestBody Counsel counsel) {
		counselService.registerCounsel(counsel);
	}

	@GetMapping("/counsels/assignment")
	public void assignCounsels(@CookieValue(value = LOGIN_COOKIE) Cookie cookie) {
		userService.checkManagerPrivileges(cookie.getValue());
		counselService.assignCounsels(cookie.getValue());
	}

	@GetMapping("/counsels")
	public int getCounselsWithoutCharger(@CookieValue(value = LOGIN_COOKIE) Cookie cookie) {
		userService.checkManagerPrivileges(cookie.getValue());
		return counselService.getCounselsWithoutCharger(cookie.getValue());
	}
}
