package com.helpmeCookies.review.service;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.repository.ProductRepository;
import com.helpmeCookies.review.dto.ReviewRequest;
import com.helpmeCookies.review.entity.Review;
import com.helpmeCookies.review.repository.ReviewRepository;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void saveReview(ReviewRequest request, Long productId) {
        User writer = userRepository.findById(request.writerId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 writerID입니다."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 productID입니다."));

        reviewRepository.save(request.toEntity(writer,product));
    }

    public void editReview(ReviewRequest request, Long productId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 reviewId 입니다."));
        review.updateContent(request.content());
    }
 }
