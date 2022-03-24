package com.heather.cs.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.heather.cs.response.code.ResponseCode;
import com.heather.cs.response.message.ResponseMessage;

import lombok.Getter;

@Getter
public class Response<T> { //성공일 경우 Static object
	private int code;
	private String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	public Response() {
		this.code = ResponseCode.SUCCESS;
		this.message = ResponseMessage.SUCCESS;
	}

	public Response(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Response(T data) {
		this.code = ResponseCode.SUCCESS;
		this.message = ResponseMessage.SUCCESS;
		this.data = data;
	}
	//
	// public Response successResponse() {
	// 	this.code = ResponseCode.SUCCESS;
	// 	this.message = ResponseMessage.SUCCESS;
	// 	return this;
	// }
	//
	// public Response errorResponse(int code, String message) {
	// 	this.code =  code;
	// 	this.message = message;
	// 	return this;
	// }
	//
	// public Response messageResponse(String message) {
	// 	this.code = ResponseCode.SUCCESS;
	// 	this.message = message;
	// 	return this;
	// }
	//
	// public Response<T> withData(T data) {
	// 	this.code = ResponseCode.SUCCESS;
	// 	this.message = ResponseMessage.SUCCESS;
	// 	this.data = data;
	// 	return this;
	// }
}
