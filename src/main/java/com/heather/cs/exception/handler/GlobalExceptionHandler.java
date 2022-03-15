package com.heather.cs.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.heather.cs.response.Response;
import com.heather.cs.response.code.ResponseCode;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final Response response;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Response handleMethodArgumentNotValid(MethodArgumentNotValidException argumentNotValidException) {
		return response.errorResponse(ResponseCode.NOT_VALID_ARGUMENT, getValidationMessage(argumentNotValidException));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public Response handleIllegalArgumentException(Exception illegalException) {
		return response.errorResponse(ResponseCode.ILLEGAL_ARGUMENT, illegalException.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalStateException.class)
	public Response handleIllegalStateException(Exception illegalException) {
		return response.errorResponse(ResponseCode.ILLEGAL_STATE, illegalException.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Response handleException(Exception exception) {
		return response.errorResponse(ResponseCode.INTERNAL_SERVER_ERROR, exception.getMessage());
	}

	public String getValidationMessage(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		return makeMessage(bindingResult);
	}

	public String makeMessage(BindingResult bindingResult) {
		String message = "";
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			message += "[";
			message += fieldError.getField();
			message += "] ";
			message += fieldError.getDefaultMessage();
			message += " (";
			message += fieldError.getField();
			message += " : ";
			message += fieldError.getRejectedValue();
			message += ") ";
		}
		return message;
	}
}
