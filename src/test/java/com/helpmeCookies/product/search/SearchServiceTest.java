package com.helpmeCookies.product.search;

import static com.helpmeCookies.product.util.SortUtil.convertProductSort;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.helpmeCookies.product.repository.ProductRepository;
import com.helpmeCookies.product.repository.dto.ProductSearch;
import com.helpmeCookies.product.service.ProductService;
import com.helpmeCookies.product.util.ProductSort;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@TestComponent
@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 검색 서비스")
    void 상품_검색() {
        // given
        var sort = convertProductSort(ProductSort.LATEST);
        var pageRequest = PageRequest.of(0, 10, sort);
        var productSearch = mock(ProductSearch.class);
        given(productSearch.getId()).willReturn(1L);
        given(productSearch.getName()).willReturn("product1");
        given(productSearch.getArtist()).willReturn("artist");
        given(productSearch.getPrice()).willReturn(10000L);
        given(productSearch.getThumbnailUrl()).willReturn("thumbnailUrl");

        var productPage = new PageImpl<>(List.of(productSearch));
        given(productRepository.findByNameWithIdx("roduct", pageRequest))
            .willReturn(productPage);

        // when
        var result = productService.getProductsByPage("roduct", pageRequest);

        // then
        assertAll(
            () -> assertThat(result.hasNext()).isFalse(),
            () -> assertThat(result.products().size()).isEqualTo(1L),
            () -> assertThat(result.products().get(0).name()).isEqualTo("product1"),
            () -> assertThat(result.products().get(0).artist()).isEqualTo("artist"),
            () -> assertThat(result.products().get(0).price()).isEqualTo(10000L),
            () -> assertThat(result.products().get(0).thumbnailUrl()).isEqualTo("thumbnailUrl")
        );
    }

}
