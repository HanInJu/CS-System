package com.heather.cs.statistics.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.category.service.CategoryService;
import com.heather.cs.response.Response;
import com.heather.cs.statistics.dto.CounselStatisticsDto;
import com.heather.cs.statistics.dto.CounselorStatisticsDto;
import com.heather.cs.statistics.dto.StatisticsRequestDto;
import com.heather.cs.statistics.service.StatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

	private final StatisticsService statisticsService;
	private final CategoryService categoryService;

	@GetMapping("/statistics/counsel")
	public Response<List<CounselStatisticsDto>> getCounselStatistics(@RequestBody StatisticsRequestDto requestDto) {
		validateDuration(requestDto);
		validateCategory(requestDto.getCategoryId());

		List<CounselStatisticsDto> statisticsList = statisticsService.getCounselStatistics(requestDto);
		return new Response<>(statisticsList);
	}

	@GetMapping("/statistics/counselor")
	public Response<List<CounselorStatisticsDto>> getCounselorStatistics(@RequestBody StatisticsRequestDto requestDto) {
		validateDuration(requestDto);
		validateCategory(requestDto.getCategoryId());

		List<CounselorStatisticsDto> statisticsList = statisticsService.getCounselorStatistics(requestDto);
		return new Response<>(statisticsList);
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
