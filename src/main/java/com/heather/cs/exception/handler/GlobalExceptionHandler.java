package com.heather.cs.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.heather.cs.response.Response;
import com.heather.cs.response.code.ResponseCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Response> handleMethodArgumentNotValid(
		MethodArgumentNotValidException argumentNotValidException) {
		Response response = new Response(ResponseCode.NOT_VALID_ARGUMENT,
			makeValidationMessage(argumentNotValidException));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Response> handleIllegalArgumentException(Exception illegalException) {
		Response response = new Response(ResponseCode.ILLEGAL_ARGUMENT,
			illegalException.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<Response> handleIllegalStateException(Exception illegalException) {
		Response response = new Response(ResponseCode.ILLEGAL_STATE,
			illegalException.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> handleException(Exception exception) {
		Response response = new Response(ResponseCode.INTERNAL_SERVER_ERROR,
			exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public String makeValidationMessage(MethodArgumentNotValidException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		StringBuilder message = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			message.append("[");
			message.append(fieldError.getField());
			message.append("] ");
			message.append(fieldError.getDefaultMessage());
			message.append(" (");
			message.append(fieldError.getField());
			message.append(" : ");
			message.append(fieldError.getRejectedValue());
			message.append(") ");
		}
		return message.toString();
	}
}
