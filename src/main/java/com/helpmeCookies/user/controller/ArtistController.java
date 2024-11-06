package com.helpmeCookies.user.controller;

import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.user.controller.apiDocs.ArtistApiDocs;
import com.helpmeCookies.user.dto.ArtistInfoPage;
import com.helpmeCookies.user.dto.request.BusinessArtistReq;
import com.helpmeCookies.user.dto.request.StudentArtistReq;
import com.helpmeCookies.user.dto.response.ArtistDetailsRes;
import com.helpmeCookies.user.service.ArtistService;
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
public class ArtistController implements ArtistApiDocs {
	private final ArtistService artistService;

	@PostMapping("/v1/artists/students")
	public ResponseEntity<String> registerStudents(
		@RequestBody StudentArtistReq artistDetailsReq,
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		artistService.registerStudentsArtist(artistDetailsReq, jwtUser.getId());
		return ResponseEntity.ok().build();
	}

	@PostMapping("/v1/artists/bussinesses")
	public ResponseEntity<String> registerbussinsess(
		@RequestBody BusinessArtistReq businessArtistReq,
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		artistService.registerBusinessArtist(businessArtistReq, jwtUser.getId());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/v1/artists/{userId}")
	public ArtistDetailsRes getArtist(
		@PathVariable Long userId
	) {
		return artistService.getArtistDetails(userId);
	}

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
