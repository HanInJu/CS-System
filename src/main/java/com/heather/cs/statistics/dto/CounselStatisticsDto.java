package com.heather.cs.statistics.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Alias("counselStatistics")
@NoArgsConstructor
@AllArgsConstructor
public class CounselStatisticsDto {

	private String date;
	private int registeredCounsels;
	private int completedCounsels;
	private int delayedCounsels;
}
