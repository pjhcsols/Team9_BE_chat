package com.helpmeCookies.product.dto;

import com.helpmeCookies.product.entity.HashTag;
import com.helpmeCookies.product.entity.Product;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String category,
        String size,
        Long price,
        String description,
        String preferredLocation,
        List<HashTag> hashTags,
        ArtistInfo artistInfo,
        List<String> imageUrls
) {
    public record ArtistInfo(Long artistId, String artistName) {
    }

    public static ProductResponse from(Product product, List<String> urls) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory().getName(),
                product.getSize(),
                product.getPrice(),
                product.getDescription(),
                product.getPreferredLocation(),
                product.getHashTags(),
                new ArtistInfo(product.getArtistInfo().getId(),product.getArtistInfo().getNickname()),
                urls
        );
    }
}
