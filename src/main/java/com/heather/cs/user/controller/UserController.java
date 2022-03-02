package com.heather.cs.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.configuration.annotation.LogInUser;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private static final String LOGIN_COOKIE = "userIdCookie";
	private static final int COOKIE_MAX_AGE = 18000;

	@PostMapping("/user")
	public void registerUser(@Valid @RequestBody User user) {
		userService.registerUser(user);
	}

	@PostMapping("/logIn")
	public void logIn(HttpServletResponse response, @RequestBody User user) {
		if(userService.isValidUser(user.getId(), user.getPassword())) {
			Cookie userIdCookie = new Cookie(LOGIN_COOKIE, user.getId());
			userIdCookie.setMaxAge(COOKIE_MAX_AGE);
			response.addCookie(userIdCookie);
		}
	}

	@PatchMapping("/user/status/on")
	public void changeStatusOn(@LogInUser User user) {
		userService.changeStatusOn(user);
	}

	@PatchMapping("/user/status/off")
	public void changeStatusOff(@LogInUser User user) {
		userService.changeStatusOff(user);
	}

}
