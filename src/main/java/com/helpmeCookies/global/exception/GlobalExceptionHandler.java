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
	public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException() {
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"해당 리소스를 찾을 수 없습니다."));
	}

	@ExceptionHandler(DuplicateRequestException.class)
	public ResponseEntity<ApiResponse<Void>> handleDuplicateRequestException() {
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,"이미 생성되었거나 중복된 요청입니다."));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST,e.getMessage()));
	}
}
