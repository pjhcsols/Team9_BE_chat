package com.helpmeCookies.review.controller;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.ApiResponse.SuccessCode;
import com.helpmeCookies.review.dto.ReviewRequest;
import com.helpmeCookies.review.dto.ReviewResponse;
import com.helpmeCookies.review.entity.Review;
import com.helpmeCookies.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //TODO 내 감상평 조회
    //TODO 해당 작가에 대한 감상평 모두 조회

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> postReview(@RequestBody ReviewRequest request, @PathVariable Long productId) {
        reviewService.saveReview(request, productId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.CREATED_SUCCESS));
    }

    @PutMapping("/{productId}/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> editReview(@RequestBody ReviewRequest request, @PathVariable Long productId, @PathVariable Long reviewId) {
        reviewService.editReview(request, productId, reviewId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.CREATED_SUCCESS));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReview(@PathVariable Long reviewId) {
        Review response = reviewService.getReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.CREATED_SUCCESS,ReviewResponse.fromEntity(response)));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteView(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.NO_CONTENT));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllReview(@RequestParam(required = false) Long productId) {
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK,reviewService.getAllReview(productId)));
    }
}
