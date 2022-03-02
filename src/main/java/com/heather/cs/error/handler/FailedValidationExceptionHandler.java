package com.heather.cs.error.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.heather.cs.error.response.ExceptionResponse;

@RestControllerAdvice
public class FailedValidationExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatus status, WebRequest request) {
		ExceptionResponse errorDetails = new ExceptionResponse("Validation Failed",
			ex.getBindingResult().toString());
		return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
	}

}
