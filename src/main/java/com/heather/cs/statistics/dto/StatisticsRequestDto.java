package com.heather.cs.statistics.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Alias("statisticsDto")
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsRequestDto {

	@NotBlank
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	@NotBlank
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	@NotNull
	private long categoryId;

	public boolean isProperPeriod() {
		System.out.println(this.startDate + " +30 " + (this.endDate.minusDays(30)));
		System.out.println(this.startDate.isAfter(this.endDate.minusDays(30)));
		return this.startDate.isAfter(this.endDate.minusDays(30));
	}

}
