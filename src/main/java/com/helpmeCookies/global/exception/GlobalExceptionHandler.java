package com.helpmeCookies.global.exception;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.helpmeCookies.global.exception.user.ResourceNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException e) {
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}

	@ExceptionHandler(DuplicateRequestException.class)
	public String handleDuplicateRequestException() {
		return "이미 생성되었거나 중복된 요청입니다.";
	}
}
