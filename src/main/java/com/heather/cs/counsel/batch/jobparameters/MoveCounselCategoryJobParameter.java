package com.heather.cs.counsel.batch.jobparameters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public class MoveCounselCategoryJobParameter {

	private LocalDate date;

	@Value("#{jobParameters[date]}")
	public void setCreateDate(String date) {
		this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).minusDays(3);
	}
}
