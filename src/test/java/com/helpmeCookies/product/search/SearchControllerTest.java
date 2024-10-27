package com.helpmeCookies.product.search;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.helpmeCookies.global.jwt.JwtProvider;
import com.helpmeCookies.product.controller.ProductController;
import com.helpmeCookies.product.dto.ProductPage;
import com.helpmeCookies.product.repository.dto.ProductSearch;
import com.helpmeCookies.product.service.ProductImageService;
import com.helpmeCookies.product.service.ProductService;
import com.helpmeCookies.product.util.ProductSort;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(JwtProvider.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductImageService productImageService;


    @Test
    @DisplayName("상품 검색 컨트롤러")
    void 상품_검색() throws Exception {
        // given
        String query = "product";
        int size = 10;
        int page = 0;
        ProductSort productSort = ProductSort.LATEST;

        var productSearch = mock(ProductSearch.class);
        given(productSearch.getId()).willReturn(1L);
        given(productSearch.getName()).willReturn("product1");
        given(productSearch.getArtist()).willReturn("artist");
        given(productSearch.getPrice()).willReturn(10000L);
        var paging = ProductPage.Paging.from(new PageImpl<>(List.of(productSearch)));
        given(productService.getProductsByPage(eq(query), any(Pageable.class)))
            .willReturn(paging);

        // when & then
        mvc.perform(get("/test/v1/products")
                .param("query", query)
                .param("size", String.valueOf(size))
                .param("page", String.valueOf(page))
                .param("sort", productSort.name()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hasNext").value(false))
            .andExpect(jsonPath("$.products[0].name").value("product1"))
            .andExpect(jsonPath("$.products[0].artist").value("artist"))
            .andExpect(jsonPath("$.products[0].price").value(10000L));
    }

}
