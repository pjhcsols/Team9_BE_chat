package com.helpmeCookies.review.dto;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.review.entity.Review;
import com.helpmeCookies.user.entity.User;

public record ReviewRequest(Long writerId, String content) {
    public Review toEntity(User writer, Product product) {
        return Review.builder()
                .content(content)
                .writer(writer)
                .product(product)
                .build();
    }
}
