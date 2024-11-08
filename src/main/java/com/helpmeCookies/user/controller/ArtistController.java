package com.helpmeCookies.user.controller;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.ApiResponse.SuccessCode;
import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.review.dto.ReviewResponse;
import com.helpmeCookies.review.service.ReviewService;
import com.helpmeCookies.user.controller.apiDocs.ArtistApiDocs;
import com.helpmeCookies.user.dto.ArtistInfoPage;
import com.helpmeCookies.user.dto.request.BusinessArtistReq;
import com.helpmeCookies.user.dto.request.StudentArtistReq;
import com.helpmeCookies.user.dto.response.ArtistDetailsRes;
import com.helpmeCookies.user.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	private final ReviewService reviewService;

    @PostMapping("/v1/artists/students")
    public ResponseEntity<ApiResponse<Void>> registerStudents(
        @RequestBody StudentArtistReq artistDetailsReq,
        @AuthenticationPrincipal JwtUser jwtUser
    ) {
        artistService.registerStudentsArtist(artistDetailsReq, jwtUser.getId());
        return ResponseEntity.ok((ApiResponse.success(SuccessCode.OK)));
    }

    @PostMapping("/v1/artists/bussinesses")
    public ResponseEntity<ApiResponse<Void>> registerbussinsess(
        @RequestBody BusinessArtistReq businessArtistReq,
        @AuthenticationPrincipal JwtUser jwtUser
    ) {
        artistService.registerBusinessArtist(businessArtistReq, jwtUser.getId());
        return ResponseEntity.ok((ApiResponse.success(SuccessCode.OK)));
    }


	@GetMapping("/v1/artists/{artistInfoId}")
	public ResponseEntity<ApiResponse<ArtistDetailsRes>> getArtistPublicDetails(
		@PathVariable Long artistInfoId,
		@AuthenticationPrincipal JwtUser jwtUser
	) {
		ArtistDetailsRes artistDetailsRes;

		if (jwtUser == null) {
			artistDetailsRes = artistService.getArtistDetails(artistInfoId);
		} else {
			artistDetailsRes = artistService.getArtistPublicDetails(artistInfoId, jwtUser.getId());
		}
		return ResponseEntity.ok((ApiResponse.success(SuccessCode.OK, artistDetailsRes)));
	}

    @GetMapping("/v1/artist")
    public ResponseEntity<ApiResponse<ArtistDetailsRes>> getArtist(
        @AuthenticationPrincipal JwtUser jwtUser
    ) {
        ArtistDetailsRes artistDetailsRes = artistService.getArtistDetails(jwtUser.getId());
        return ResponseEntity.ok((ApiResponse.success(SuccessCode.OK, artistDetailsRes)));
    }

    @GetMapping("/v1/artists/search")
    public ResponseEntity<ApiResponse<ArtistInfoPage.Paging>> getArtistsByPage(
        @RequestParam("query") String query,
        @RequestParam(name = "size", required = false, defaultValue = "20") int size,
        @RequestParam("page") int page,
        @AuthenticationPrincipal JwtUser jwtUser
    ) {
        var pageable = PageRequest.of(page, size);
        var userId = jwtUser == null ? null : jwtUser.getId();
        ArtistInfoPage.Paging artistInfoPage = artistService.getArtistsByPage(query, pageable, userId);
        return ResponseEntity.ok((ApiResponse.success(SuccessCode.OK, artistInfoPage)));
    }

	@GetMapping("/v1/artists/{artistId}/reviews")
	public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllReviewsByArtist(
			@PathVariable("artistId") Long artistId,
			@PageableDefault(size = 7) Pageable pageable) {
		return ResponseEntity.ok(ApiResponse.success(
				SuccessCode.OK,reviewService.getAllReviewByArtist(artistId, pageable)));
	}
}
