package com.heather.cs.statistics.dto;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Alias("counselorStatistics")
@NoArgsConstructor
@AllArgsConstructor
public class CounselorStatisticsDto {

	private String date;
	private String counselorId;
	private int completedCounsels;
	private int counselsProcessedNormally;
	private int delayedCounsels;
}
