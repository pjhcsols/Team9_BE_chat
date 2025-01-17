package com.helpmeCookies.product.dto;

import com.helpmeCookies.product.entity.Category;
import com.helpmeCookies.product.entity.HashTag;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.user.entity.ArtistInfo;

import java.util.List;

public record ProductRequest(
        String name,
        String category,
        String size,
        Long price,
        String description,
        String preferredLocation,
        List<HashTag> hashTags,
        Long artistInfoId,
        List<String> imageUrls

) {
    public Product toEntity(ArtistInfo artistInfo,String thumbnailImage) {
        return Product.builder()
                .name(name)
                .category(Category.fromString(category))
                .size(size)
                .price(price)
                .description(description)
                .preferredLocation(preferredLocation)
                .hashTags(hashTags)
                .artistInfo(artistInfo)
                .thumbnailUrl(thumbnailImage)
                .build();
    }

    public String getThumbnailImage() {
        return imageUrls.get(0);
    }
}
