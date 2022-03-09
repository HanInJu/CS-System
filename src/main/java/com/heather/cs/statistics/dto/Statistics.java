package com.heather.cs.statistics.dto;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Alias("statistics")
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {

	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int registeredCounsels;

	private int completedCounsels;

	private int delayedCounsels;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String date;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int counselsProcessedNormally;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String counselorId;

}
