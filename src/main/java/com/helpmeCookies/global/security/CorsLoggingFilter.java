package com.helpmeCookies.global.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorsLoggingFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(CorsLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		// 요청 헤더 로깅, cors
		String origin = request.getHeader("Origin");
		if (origin != null) {
			logger.info("Request Origin Header: {}", origin);
		}

		String requestMethod = request.getHeader("cross-control-allow-origin");
		if (requestMethod != null) {
			logger.info("cross-control-allow-origin: {}", requestMethod);
		}

		// 필터 체인을 통해 요청 처리
		filterChain.doFilter(request, response);

		// cors 관련 응답 헤더 로깅
		String responseHeader = response.getHeader("Access-Control-Allow-Origin");
		if (responseHeader != null) {
			logger.info("Response Access-Control-Allow-Origin Header: {}", responseHeader);
		}
	}
}
