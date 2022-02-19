package com.heather.cs.user.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.user.dto.User;
import com.heather.cs.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/user")
	public void registerUser(@RequestBody User user) {
		userService.registerUser(user);
	}

	@PatchMapping("/user/counselor/{userId}/status")
	public void changeTheCounselorStatus(@PathVariable String userId, @RequestParam String state) {
		userService.changeTheCounselorStatus(userId, state);
	}

}