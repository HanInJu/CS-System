package com.heather.cs.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DateConverter implements Converter<String, LocalDateTime> {

	@Override
	public LocalDateTime convert(String source) {
		return LocalDateTime.parse(source, DateTimeFormatter.ISO_DATE);
	}

}
