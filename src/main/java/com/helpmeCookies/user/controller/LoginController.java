package com.helpmeCookies.user.controller;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpmeCookies.global.jwt.JwtProvider;
import com.helpmeCookies.global.jwt.JwtToken;
import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.global.security.UserDetailService;
import com.helpmeCookies.product.entity.HashTag;
import com.helpmeCookies.user.dto.KakaoOAuth2Response;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.entity.UserInfo;
import com.helpmeCookies.user.repository.UserRepository;
import com.helpmeCookies.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//Todo: Swagger 추가
public class LoginController {
	private final UserRepository userRepository;
	private final UserDetailService userDetailsService;
	private final JwtProvider jwtProvider;

	// 임시 회원가입 url. 유저를 생성하고 jwt 토큰을 반환한다.
	@GetMapping("/test/signup")
	public JwtToken signup() {
		UserInfo userInfo = UserInfo.builder()
			.email("test@test")
			.birthdate("1995-01-01")
			.phone("010-1234-5678")
			.hashTags(List.of(HashTag.DREAMLIKE))
			.name("test")
			.address("서울시 강남구")
			.build();

		User user = User.builder()
			.userInfo(userInfo)
			.nickname("test")
			.userImageUrl("test")
			.build();

		userRepository.save(user);
		return jwtProvider.createToken(JwtUser.of(user.getId()));
	}

	@GetMapping("/oauth2/login/kakao")
	public JwtToken ttt(@AuthenticationPrincipal OAuth2User oAuth2User) {
		KakaoOAuth2Response kakaoUser = KakaoOAuth2Response.from(oAuth2User.getAttributes());
		return jwtProvider.createToken(userDetailsService.loadUserByEmail(kakaoUser.email(), kakaoUser.nickname()));
	}
}
