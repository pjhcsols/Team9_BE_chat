package com.helpmeCookies.global.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
	private String secret;
	private long accessTokenExpireTime;
	private long refreshTokenExpireTime;

	public JwtProperties() {
	}
}
