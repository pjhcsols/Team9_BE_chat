package com.helpmeCookies.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//			.allowedOrigins("http://1.618.s3-website.ap-northeast-2.amazonaws.com", "http://localhost:3000")
//			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//			.allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers","cross-control-allow-origin")
//			.allowCredentials(true);
//	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOriginPatterns("http://1.618.s3-website.ap-northeast-2.amazonaws.com/") // 허용할 도메인 (모든 도메인 허용: "*")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
			.allowedHeaders("*") // 허용할 헤더
			.allowCredentials(true); // 인증 정보 허용 여부
	}
}