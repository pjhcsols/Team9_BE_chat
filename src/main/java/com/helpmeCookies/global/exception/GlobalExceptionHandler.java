package com.helpmeCookies.global.exception;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.helpmeCookies.global.exception.user.DuplicateResourceException;
import com.helpmeCookies.global.exception.user.ResourceNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException e) {
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ApiResponse<Void>> handleDuplicateRequestException(DuplicateResourceException e) {
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, e.getMessage()));
	}
}
