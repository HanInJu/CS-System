package com.heather.cs.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.heather.cs.configuration.annotation.LogInUser;
import com.heather.cs.response.Response;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final Response response;
	private final UserService userService;
	private static final String LOGIN_COOKIE = "userIdCookie";
	private static final int COOKIE_MAX_AGE = 18000;

	@PostMapping("/user")
	public Response registerUser(@Valid @RequestBody User user) {
		userService.registerUser(user);
		return response.successResponse();
	}

	@PostMapping("/logIn")
	public Response logIn(HttpServletResponse response, @RequestBody User user) {
		if(userService.isValidUser(user.getId(), user.getPassword())) {
			Cookie userCookie = generateCookie(LOGIN_COOKIE, user.getId(), COOKIE_MAX_AGE);
			response.addCookie(userCookie);
		}
		return this.response.successResponse();
	}

	@PatchMapping("/user/status/on")
	public Response changeStatusOn(@LogInUser User user) {
		userService.changeStatusOn(user);
		return response.successResponse();
	}

	@PatchMapping("/user/status/off")
	public Response changeStatusOff(@LogInUser User user) {
		userService.changeStatusOff(user);
		return response.successResponse();
	}


	public Cookie generateCookie(String cookieName, String cookieValue, int maxAge) {
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(maxAge);
		return cookie;
	}
}
