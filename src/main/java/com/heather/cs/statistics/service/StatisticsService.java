package com.heather.cs.statistics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.heather.cs.statistics.dto.Statistics;
import com.heather.cs.statistics.dto.StatisticsRequestDto;
import com.heather.cs.statistics.mapper.StatisticsMapper;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {

	private final StatisticsMapper statisticsMapper;

	public List<Statistics> getCounselStatistics(User user, StatisticsRequestDto requestDto) {
		List<Statistics> statisticsList = statisticsMapper.selectCounselStatistics(requestDto);
		return statisticsList;
	}

	public List<Statistics> getCounselorStatistics(User user, StatisticsRequestDto requestDto) {
		List<Statistics> statisticsList = statisticsMapper.selectCounselorStatistics(requestDto);

		return statisticsList;
	}
}
