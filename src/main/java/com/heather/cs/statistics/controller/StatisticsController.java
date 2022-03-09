package com.heather.cs.statistics.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.category.service.CategoryService;
import com.heather.cs.configuration.annotation.LogInUser;
import com.heather.cs.response.Response;
import com.heather.cs.response.code.ResponseCode;
import com.heather.cs.response.message.ResponseMessage;
import com.heather.cs.statistics.dto.Statistics;
import com.heather.cs.statistics.dto.StatisticsRequestDto;
import com.heather.cs.statistics.service.StatisticsService;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

	private final StatisticsService statisticsService;
	private final CategoryService categoryService;

	@GetMapping("/statistics/counsel")
	public ResponseEntity<Response<Object>> getCounselStatistics(@LogInUser User user,
		@RequestBody StatisticsRequestDto requestDto) {
		if (!requestDto.isProperPeriod()) {
			String message = "The duration must be less than 30 days. (startDate : "
				+ requestDto.getStartDate()
				+ ", endDate : "
				+ requestDto.getEndDate()
				+ ")";
			return new ResponseEntity<>(
				new Response<>(ResponseCode.NOT_PROPER_DURATION, message),
				HttpStatus.BAD_REQUEST);
		}
		if (!categoryService.isExistCategory(requestDto.getCategoryId())) {
			String message = "Invalid Category (categoryId : " + requestDto.getCategoryId() + ")";
			return new ResponseEntity<>(
				new Response<>(ResponseCode.NOT_VALID_CATEGORY, message),
				HttpStatus.BAD_REQUEST);
		}
		List<Statistics> statisticsList = statisticsService.getCounselStatistics(user, requestDto);
		return new ResponseEntity<>(
			new Response<>(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, statisticsList),
			HttpStatus.OK);
	}

	@GetMapping("/statistics/counselor")
	public ResponseEntity<Response<List<Statistics>>> getCounselorStatistics(@LogInUser User user,
		@RequestBody StatisticsRequestDto requestDto) {
		if (!requestDto.isProperPeriod()) {
			String message = "The duration must be less than 30 days. (startDate : "
				+ requestDto.getStartDate()
				+ ", endDate : "
				+ requestDto.getEndDate()
				+ ")";
			return new ResponseEntity<>(
				new Response<>(ResponseCode.NOT_PROPER_DURATION, message),
				HttpStatus.BAD_REQUEST);
		}
		if (!categoryService.isExistCategory(requestDto.getCategoryId())) {
			String message = "Invalid Category (categoryId : " + requestDto.getCategoryId() + ")";
			return new ResponseEntity<>(
				new Response<>(ResponseCode.NOT_VALID_CATEGORY, message),
				HttpStatus.BAD_REQUEST);
		}
		List<Statistics> statisticsList = statisticsService.getCounselorStatistics(user, requestDto);
		return new ResponseEntity<>(
			new Response<>(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, statisticsList),
			HttpStatus.OK);
	}

}
