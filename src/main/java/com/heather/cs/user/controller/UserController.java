package com.heather.cs.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.configuration.annotation.LogInUser;
import com.heather.cs.response.Response;
import com.heather.cs.response.code.ResponseCode;
import com.heather.cs.response.message.ResponseMessage;
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
	public ResponseEntity<Response> registerUser(@Valid @RequestBody User user) {
		userService.registerUser(user);
		return new ResponseEntity<>(new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS), HttpStatus.OK);
	}

	@PostMapping("/logIn")
	public ResponseEntity<Response> logIn(HttpServletResponse response, @RequestBody User user) {
		if(userService.isValidUser(user.getId(), user.getPassword())) {
			Cookie userIdCookie = new Cookie(LOGIN_COOKIE, user.getId());
			userIdCookie.setMaxAge(COOKIE_MAX_AGE);
			response.addCookie(userIdCookie);
		}
		return new ResponseEntity<>(new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS), HttpStatus.OK);
	}

	@PatchMapping("/user/status/on")
	public ResponseEntity<Response> changeStatusOn(@LogInUser User user) {
		userService.changeStatusOn(user);
		return new ResponseEntity<>(new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS), HttpStatus.OK);
	}

	@PatchMapping("/user/status/off")
	public ResponseEntity<Response> changeStatusOff(@LogInUser User user) {
		userService.changeStatusOff(user);
		return new ResponseEntity<>(new Response(ResponseCode.SUCCESS, ResponseMessage.SUCCESS), HttpStatus.OK);
	}

}
