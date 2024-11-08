package com.helpmeCookies.review.service;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.repository.ProductRepository;
import com.helpmeCookies.review.dto.ReviewRequest;
import com.helpmeCookies.review.dto.ReviewResponse;
import com.helpmeCookies.review.entity.Review;
import com.helpmeCookies.review.repository.ReviewRepository;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void saveReview(ReviewRequest request, Long productId) {
        User writer = userRepository.findById(request.writerId()).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 writerID입니다."));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 productID입니다."));

        reviewRepository.save(request.toEntity(writer,product));
    }

    @Transactional
    public void editReview(ReviewRequest request, Long productId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 reviewId 입니다."));
        review.updateContent(request.content());
    }

    @Transactional(readOnly = true)
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 reviewId 입니다."));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReview(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 productId입니다." + productId));
        return reviewRepository.findAllByProduct(product).map(ReviewResponse::fromEntity);
    }
 }
