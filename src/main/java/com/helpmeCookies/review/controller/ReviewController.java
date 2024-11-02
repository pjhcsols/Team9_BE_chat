package com.helpmeCookies.review.controller;

import com.helpmeCookies.review.dto.ReviewRequest;
import com.helpmeCookies.review.dto.ReviewResponse;
import com.helpmeCookies.review.entity.Review;
import com.helpmeCookies.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //TODO 감상평 삭제
    //TODO 해당 상품의 감상평 조회
    //TODO 내 감상평 조회

    @PostMapping("/{productId}")
    public ResponseEntity<Void> postReview(@RequestBody ReviewRequest request, @PathVariable Long productId) {
        reviewService.saveReview(request, productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/{reviewId}")
    public ResponseEntity<Void> editReview(@RequestBody ReviewRequest request, @PathVariable Long productId, @PathVariable Long reviewId) {
        reviewService.editReview(request, productId, reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long reviewId) {
        Review response = reviewService.getReview(reviewId);
        return ResponseEntity.ok(ReviewResponse.fromEntity(response));
    }
}
