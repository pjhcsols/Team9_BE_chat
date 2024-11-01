package com.helpmeCookies.user.controller;

import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.user.controller.apiDocs.ArtistApiDocs;
import com.helpmeCookies.user.dto.ArtistInfoPage;
import com.helpmeCookies.user.dto.request.BusinessArtistReq;
import com.helpmeCookies.user.dto.request.StudentArtistReq;
import com.helpmeCookies.user.dto.response.ArtistDetailsRes;
import com.helpmeCookies.user.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "작가 관련 기능", description = "작가 관련 API")
public class ArtistController implements ArtistApiDocs {
	private final ArtistService artistService;

	@Operation(summary = "학생 작가 등록", description = "학생 작가 등록")
	@PostMapping("/v1/artists/students")
	public ResponseEntity<String> registerStudents(
		@RequestBody StudentArtistReq artistDetailsReq,
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		artistService.registerStudentsArtist(artistDetailsReq, jwtUser.getId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "사업자 작가 등록", description = "사업자 작가 등록")
	@PostMapping("/v1/artists/bussinesses")
	public ResponseEntity<String> registerbussinsess(
		@RequestBody BusinessArtistReq businessArtistReq,
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		artistService.registerBusinessArtist(businessArtistReq, jwtUser.getId());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "작가 프로필 조회", description = "작가 프로필 조회")
	@GetMapping("/v1/artists/{userId}")
	public ArtistDetailsRes getArtist(
		@AuthenticationPrincipal JwtUser jwtUser,
		@PathVariable Long userId
	) {
		return artistService.getArtistDetails(userId);
	}

	@Operation(summary = "작가 프로필 조회", description = "자신의 작가 프로필 조회")
	@GetMapping("/v1/artist")
	public ArtistDetailsRes getArtist(
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		return artistService.getArtistDetails(jwtUser.getId());
	}

	@GetMapping("/v1/artists")
	public ResponseEntity<ArtistInfoPage.Paging> getArtistsByPage(
		@RequestParam("query") String query,
		@RequestParam(name = "size", required = false, defaultValue = "20") int size,
		@RequestParam("page") int page
	) {
		var pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(artistService.getArtistsByPage(query, pageable));
	}
}
