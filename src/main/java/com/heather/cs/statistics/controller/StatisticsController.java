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
	private final Response response;

	@GetMapping("/statistics/counsel")
	public Response<List<Statistics>> getCounselStatistics(@RequestBody StatisticsRequestDto requestDto) {
		validateDuration(requestDto);
		validateCategory(requestDto.getCategoryId());

		List<Statistics> statisticsList = statisticsService.getCounselStatistics(requestDto);
		return response.withData(statisticsList);
	}

	@GetMapping("/statistics/counselor")
	public Response<List<Statistics>> getCounselorStatistics(@RequestBody StatisticsRequestDto requestDto) {
		validateDuration(requestDto);
		validateCategory(requestDto.getCategoryId());

		List<Statistics> statisticsList = statisticsService.getCounselorStatistics(requestDto);
		return response.withData(statisticsList);
	}

	public String makeDurationExceptionMessage(StatisticsRequestDto requestDto) {
		return "The duration must be less than 30 days. (startDate : "
			+ requestDto.getStartDate()
			+ ", endDate : "
			+ requestDto.getEndDate()
			+ ")";
	}

	public void validateDuration(StatisticsRequestDto requestDto) {
		if (!requestDto.isProperPeriod()) {
			String message = makeDurationExceptionMessage(requestDto);
			throw new IllegalArgumentException(message);
		}
	}

	public void validateCategory(long categoryId) {
		if (!categoryService.isExistCategory(categoryId)) {
			throw new IllegalArgumentException("Invalid Category (categoryId : " + categoryId + ")");
		}
	}

}
