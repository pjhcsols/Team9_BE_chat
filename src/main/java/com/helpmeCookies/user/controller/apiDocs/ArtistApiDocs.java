package com.helpmeCookies.user.controller.apiDocs;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.user.dto.ArtistInfoPage;
import com.helpmeCookies.user.dto.request.BusinessArtistReq;
import com.helpmeCookies.user.dto.request.StudentArtistReq;
import com.helpmeCookies.user.dto.response.ArtistDetailsRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "작가 관련 기능", description = "작가 관련 API, 작가 프로필 조회 API를 제외한 모든 API는 인증 이후 사용할 수 있습니다.(Authorization: Bearer {token}이 필요합니다.)")
public interface ArtistApiDocs {

	@Operation(summary = "학생 작가 등록", description = "학생 작가 등록")
	@PostMapping("/v1/artists/students")
	ResponseEntity<ApiResponse<Void>> registerStudents(
		@RequestBody StudentArtistReq artistDetailsReq,
		@AuthenticationPrincipal JwtUser jwtUser
	);

	@Operation(summary = "사업자 작가 등록", description = "사업자 작가 등록")
	@PostMapping("/v1/artists/bussinesses")
	ResponseEntity<ApiResponse<Void>> registerbussinsess(
		@RequestBody BusinessArtistReq businessArtistReq,
		@AuthenticationPrincipal JwtUser jwtUser
	);

	@Operation(summary = "작가 프로필 조회", description = "작가 프로필 조회")
	@GetMapping("/v1/artists/{userId}")
	ResponseEntity<ApiResponse<ArtistDetailsRes>> getArtist(
		@PathVariable Long userId
	);

	@Operation(summary = "작가 자신의 프로필 조회", description = "작가 자신의 프로필 조회")
	@GetMapping("/v1/artist")
	ResponseEntity<ApiResponse<ArtistDetailsRes>> getArtist(
		@AuthenticationPrincipal JwtUser jwtUser
	);

	@Operation(summary = "작가 검색")
	ResponseEntity<ApiResponse<ArtistInfoPage.Paging>> getArtistsByPage(
		String query,
		@Parameter(description = "default value 20") int size,
		int page,
		@Parameter(hidden = true) JwtUser jwtUser
	);
}
