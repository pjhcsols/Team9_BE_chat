package com.helpmeCookies.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.ApiResponse.SuccessCode;
import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.user.controller.apiDocs.UserApiDocs;
import com.helpmeCookies.user.dto.UserDto;
import com.helpmeCookies.user.dto.response.UserCommonInfoRes;
import com.helpmeCookies.user.dto.request.UserReq;
import com.helpmeCookies.user.dto.response.UserDetailsInfoRes;
import com.helpmeCookies.user.dto.UserTypeDto;
import com.helpmeCookies.user.dto.response.UserFollowingRes;
import com.helpmeCookies.user.dto.response.UserImageResponse;
import com.helpmeCookies.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApiDocs {
	private final UserService userService;


	@GetMapping("/v1/users")
	public ResponseEntity<ApiResponse<UserCommonInfoRes>> getUsers(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		UserCommonInfoRes userCommonInfoRes = UserCommonInfoRes.fromDto(userService.getUserInfo(jwtUser.getId()));
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, userCommonInfoRes));
	}

	@GetMapping("/v1/users/details")
	public ResponseEntity<ApiResponse<UserDetailsInfoRes>> getUserDetails(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		UserDetailsInfoRes userDetailsInfoRes = UserDetailsInfoRes.fromDto(userService.getUserInfo(jwtUser.getId()));

		return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, userDetailsInfoRes));
	}

	@GetMapping("/v1/users/type")
	public ResponseEntity<ApiResponse<UserTypeDto>> getUserType(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		UserTypeDto userTypeDto = userService.getUserType(jwtUser.getId());
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, userTypeDto));
	}

	@PutMapping("/v1/users")
	public ResponseEntity<ApiResponse<UserDto>> updateUser(
		@AuthenticationPrincipal JwtUser jwtUser,
		@RequestBody UserReq userReq
	) {
		// UserInfoDto를 통해서 유저 정보를 수정한다.
		UserDto userDto = userService.updateUser(userReq.toUserCommonInfoDto(), userReq.toUserInfoDto(), jwtUser.getId());
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, userDto));
	}

	@PostMapping("/v1/users/following/{artistId}")
	public ResponseEntity<ApiResponse<Void>> followArtist(
		@AuthenticationPrincipal JwtUser jwtUser,
		@PathVariable Long artistId
	) {
		userService.followArtist(jwtUser.getId(), artistId);
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
	}

	@DeleteMapping("/v1/users/following/{artistId}")
	public ResponseEntity<ApiResponse<Void>> unfollowArtist(
		@AuthenticationPrincipal JwtUser jwtUser,
		@PathVariable Long artistId
	) {
		userService.unfollowArtist(jwtUser.getId(), artistId);
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
	}

	@GetMapping("/v1/users/following")
	public ResponseEntity<ApiResponse<Page<UserFollowingRes>>> getFollowingList(
		@AuthenticationPrincipal JwtUser jwtUser,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		Page<UserFollowingRes> followingList = userService.getFollowingWithPaging(jwtUser.getId(), pageable);
		return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, followingList));
	}

	@DeleteMapping("/v1/users")
	public String deleteUser(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		return "ok";
	}
}
