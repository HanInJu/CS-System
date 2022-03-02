package com.heather.cs.answer.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.configuration.annotation.LogInUser;
import com.heather.cs.answer.dto.Answer;
import com.heather.cs.answer.service.AnswerService;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AnswerController {

	private final AnswerService answerService;

	@PostMapping("/answer/counsel")
	public void registerAnswerForCounsel(@Valid @RequestBody Answer answer, @LogInUser User user) {
		answerService.registerAnswerForCounsel(answer, user);
	}
}
