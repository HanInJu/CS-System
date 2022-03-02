package com.heather.cs.error.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
	private String message;
	private String details;
}