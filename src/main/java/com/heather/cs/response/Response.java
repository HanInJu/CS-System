package com.heather.cs.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
public class Response<T> {
	private int code;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public Response(int code, String message) {
		this.code =  code;
		this.message = message;
	}

	public Response(int code, String message, T data) {
		this.code =  code;
		this.message = message;
		this.data = data;
	}
}
