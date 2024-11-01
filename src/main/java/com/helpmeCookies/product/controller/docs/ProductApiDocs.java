package com.helpmeCookies.product.controller.docs;

import com.helpmeCookies.product.dto.ProductPage.Paging;
import com.helpmeCookies.product.util.ProductSort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "상품 관련 기능", description = "상품 관련 API")
public interface ProductApiDocs {

    @Operation(summary = "상품 검색")
    ResponseEntity<Paging> getProductsByPage(
        String query,
        @Parameter(description = "default value 20") int size,
        int page,
        ProductSort productSort
    );

}
