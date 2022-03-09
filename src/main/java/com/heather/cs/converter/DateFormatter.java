package com.heather.cs.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

public class DateFormatter implements Formatter<LocalDateTime> {

	@Override
	public LocalDateTime parse(String date, Locale locale) {
		return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE);
	}

	@Override
	public String print(LocalDateTime localDateTime, Locale locale) {
		return localDateTime.toString();
	}
}
