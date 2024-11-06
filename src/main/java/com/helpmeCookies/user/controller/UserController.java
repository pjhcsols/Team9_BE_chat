package com.helpmeCookies.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.product.dto.FileUploadResponse;
import com.helpmeCookies.product.dto.ProductImageResponse;
import com.helpmeCookies.user.controller.apiDocs.UserApiDocs;
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
	public ResponseEntity<UserCommonInfoRes> getUsers(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		return ResponseEntity.ok(UserCommonInfoRes.fromDto(userService.getUserInfo(jwtUser.getId())));
	}

	@GetMapping("/v1/users/details")
	public ResponseEntity<UserDetailsInfoRes> getUserDetails(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		return ResponseEntity.ok(UserDetailsInfoRes.fromDto(userService.getUserInfo(jwtUser.getId())));
	}

	@GetMapping("/v1/users/type")
	public ResponseEntity<UserTypeDto> getUserType(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		return ResponseEntity.ok(userService.getUserType(jwtUser.getId()));
	}

	@PostMapping("/images")
	public ResponseEntity<UserImageResponse> uploadImages(List<MultipartFile> files) throws
		IOException {
		List<FileUploadResponse> responses = userService.uploadMultiFiles(files);
		return ResponseEntity.ok(new UserImageResponse(responses.stream().map(FileUploadResponse::photoUrl).toList()));
	}

	@PutMapping("/v1/users")
	public String updateUser(
		@AuthenticationPrincipal JwtUser jwtUser,
		@RequestBody UserReq userReq
	) {
		// UserInfoDto를 통해서 유저 정보를 수정한다.
		userService.updateUser(userReq.toUserCommonInfoDto(), userReq.toUserInfoDto(), jwtUser.getId());
		return "ok";
	}

	@PostMapping("/v1/users/following/{artistId}")
	public ResponseEntity<String> followArtist(
		@AuthenticationPrincipal JwtUser jwtUser,
		@PathVariable Long artistId
	) {
		userService.followArtist(jwtUser.getId(), artistId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/v1/users/following/{artistId}")
	public ResponseEntity<String> unfollowArtist(
		@AuthenticationPrincipal JwtUser jwtUser,
		@PathVariable Long artistId
	) {
		userService.unfollowArtist(jwtUser.getId(), artistId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/v1/users/following")
	public ResponseEntity<Page<UserFollowingRes>> getFollowingList(
		@AuthenticationPrincipal JwtUser jwtUser,
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity.ok(userService.getFollowingWithPaging(jwtUser.getId(), pageable));
	}

	@DeleteMapping("/v1/users")
	public String deleteUser(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		return "ok";
	}
}
