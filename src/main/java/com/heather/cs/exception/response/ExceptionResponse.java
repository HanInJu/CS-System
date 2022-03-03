package com.heather.cs.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {
	private String requestURI; // 필요없음
	private String details;
}
