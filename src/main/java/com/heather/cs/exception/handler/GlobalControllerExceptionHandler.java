package com.heather.cs.exception.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.heather.cs.exception.response.ExceptionResponse;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException argumentNotValidException, HttpServletRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(request.getRequestURI(),
			argumentNotValidException.getBindingResult().toString());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
	public ResponseEntity<Object> handleIllegalException(Exception illegalException, // 제네릭에 Object를 쓰는 건 의미가 없음
		HttpServletRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(request.getRequestURI(),
			illegalException.getMessage()); // 내부 메시지인데 외부로 반출하지 말기
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
		// 메시지만 남길거면 굳이 엔티티 객체를 만들어서 리턴할 필요가 없음
		// 넘길 때 규격을 정해서 줘야 하는데
		// 다른 오픈API를 보면 규격이 있는데 그 규격을 참고해서 일정한 규격으로 리턴하기 바람
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(Exception exception, HttpServletRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(request.getRequestURI(),
			exception.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
