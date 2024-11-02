package com.helpmeCookies.review.dto;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.review.entity.Review;
import com.helpmeCookies.user.entity.User;

public record ReviewResponse(Long id, String content,User writer, Product product) {
    public static ReviewResponse fromEntity(Review review) {
        return new ReviewResponse(review.getId(), review.getContent(),review.getWriter(), review.getProduct());
    }
}
