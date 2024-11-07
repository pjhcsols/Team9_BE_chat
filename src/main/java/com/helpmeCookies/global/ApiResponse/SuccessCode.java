package com.helpmeCookies.global.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    OK(200, "OK"),
    CREATED_SUCCESS(201, "저장에 성공했습니다");

    private final int code;
    private final String message;

}
