package com.helpmeCookies.product.dto;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.repository.dto.ProductSearch;
import java.util.List;
import org.springframework.data.domain.Page;

public class ProductPage {

    public record Info(
            Long id,
            String name,
            String artist,
            Long price,
            String thumbnailUrl
    ) {
        public static Info from(ProductSearch productSearch) {
            return new Info(
                    productSearch.getId(),
                    productSearch.getName(),
                    productSearch.getArtist(),
                    productSearch.getPrice(),
                    productSearch.getThumbnailUrl()
            );
        }

        public static Info fromProduct(Product product) {
            return new Info(
                    product.getId(),
                    product.getName(),
                    product.getArtistInfo().getNickname(),
                    product.getPrice(),
                    product.getThumbnailUrl()
            );
        }

        public static List<Info> of(List<ProductSearch> content) {
            return content.stream()
                    .map(Info::from)
                    .toList();
        }
    }

    public record Paging (
            boolean hasNext,
            List<Info> products
    ) {

        public static Paging from(Page<ProductSearch> productPage) {
            return new Paging(
                    productPage.hasNext(),
                    Info.of(productPage.getContent())
            );
        }

        public static Paging fromProduct(Page<Product> productPage) {
            return new Paging(
                    productPage.hasNext(),
                    productPage.map(Info::fromProduct).toList()
            );
        }
    }

}
