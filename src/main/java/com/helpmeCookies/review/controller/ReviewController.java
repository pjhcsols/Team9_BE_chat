package com.helpmeCookies.review.controller;

import com.helpmeCookies.review.dto.ReviewRequest;
import com.helpmeCookies.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    //TODO 감상평 수정
    //TODO 감상평 삭제
    //TODO 해당 상품의 감상평 조회
    //TODO 내 감상평 조회

    @PostMapping("/{productId}")
    public ResponseEntity<Void> postReview(@RequestBody ReviewRequest request, @PathVariable Long productId) {
        reviewService.saveReview(request,productId);
        return ResponseEntity.ok().build();
    }
}
