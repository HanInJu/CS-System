package com.heather.cs.error.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.heather.cs.error.response.ExceptionResponse;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException argumentNotValidException, HttpServletRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(request.getRequestURI(),
			argumentNotValidException.getBindingResult().toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException,
		HttpServletRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(request.getRequestURI(),
			illegalArgumentException.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Object> handleIllegalStateException(IllegalStateException illegalStateException,
		HttpServletRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(request.getRequestURI(),
			illegalStateException.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(request.getRequestURI(),
			exception.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
