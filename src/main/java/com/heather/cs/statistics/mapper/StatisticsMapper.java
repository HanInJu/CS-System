package com.heather.cs.statistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.statistics.dto.Statistics;
import com.heather.cs.statistics.dto.StatisticsRequestDto;

@Mapper
public interface StatisticsMapper {
	List<Statistics> selectCounselStatistics(StatisticsRequestDto statisticsRequestDto);
	List<Statistics> selectCounselorStatistics(StatisticsRequestDto statisticsRequestDto);
}
