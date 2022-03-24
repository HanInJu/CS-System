package com.heather.cs.statistics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.heather.cs.statistics.dto.CounselStatisticsDto;
import com.heather.cs.statistics.dto.CounselorStatisticsDto;
import com.heather.cs.statistics.dto.StatisticsRequestDto;
import com.heather.cs.statistics.mapper.StatisticsMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {

	private final StatisticsMapper statisticsMapper;

	public List<CounselStatisticsDto> getCounselStatistics(StatisticsRequestDto requestDto) {
		return statisticsMapper.selectCounselStatistics(requestDto);
	}

	public List<CounselorStatisticsDto> getCounselorStatistics(StatisticsRequestDto requestDto) {
		return statisticsMapper.selectCounselorStatistics(requestDto);
	}
}
