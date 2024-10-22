package com.helpmeCookies.product.dto;

import com.helpmeCookies.product.entity.ProductImage;

public record ImageUpload(
        String photoUrl
) {
    public ProductImage toEntity(Long productId) {
        return ProductImage.builder()
                .productId(productId)
                .photoUrl(photoUrl)
                .build();
    }
}
