package com.heather.cs.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
	public void registerUser(@RequestBody User user) {
		userService.registerUser(user);
	}

	@PostMapping("/logIn")
	public void logIn(HttpServletResponse response, @RequestBody User user) {
		if(userService.logIn(user.getId(), user.getPassword())) {
			Cookie userIdCookie = new Cookie(LOGIN_COOKIE, user.getId());
			userIdCookie.setMaxAge(COOKIE_MAX_AGE);
			response.addCookie(userIdCookie);
		}
	}

	@PatchMapping("/user/status/on")
	public void changeStatusOn(@CookieValue(value = LOGIN_COOKIE) Cookie cookie) {
		userService.changeStatusOn(cookie.getValue());
	}

	@PatchMapping("/user/status/off")
	public void changeStatusOff(@CookieValue(value = LOGIN_COOKIE) Cookie cookie) {
		userService.changeStatusOff(cookie.getValue());
	}

}
