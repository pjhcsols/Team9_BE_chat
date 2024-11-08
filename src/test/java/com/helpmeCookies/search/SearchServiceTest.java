package com.helpmeCookies.search;

import static com.helpmeCookies.product.util.SortUtil.convertProductSort;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.helpmeCookies.product.repository.ProductRepository;
import com.helpmeCookies.product.repository.dto.ProductSearch;
import com.helpmeCookies.product.service.ProductService;
import com.helpmeCookies.product.util.ProductSort;
import com.helpmeCookies.user.repository.ArtistInfoRepository;
import com.helpmeCookies.user.repository.dto.ArtistSearch;
import com.helpmeCookies.user.service.ArtistService;
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

    @Mock
    private ArtistInfoRepository artistInfoRepository;

    @InjectMocks
    private ArtistService artistService;

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
            () -> assertThat(result.products().getFirst().name()).isEqualTo("product1"),
            () -> assertThat(result.products().getFirst().artist()).isEqualTo("artist"),
            () -> assertThat(result.products().getFirst().price()).isEqualTo(10000L),
            () -> assertThat(result.products().getFirst().thumbnailUrl()).isEqualTo("thumbnailUrl")
        );
    }

    @Test
    @DisplayName("작가 검색 서비스")
    void 작가_검색() {
        // given
        var pageRequest = PageRequest.of(0, 10);
        var artistSearch = mock(ArtistSearch.class);
        given(artistSearch.getId()).willReturn(1L);
        given(artistSearch.getNickname()).willReturn("nickname");
        given(artistSearch.getArtistImageUrl()).willReturn("artistImageUrl");
        given(artistSearch.getTotalFollowers()).willReturn(10000L);
        given(artistSearch.getTotalLikes()).willReturn(10000L);

        var artistPage = new PageImpl<>(List.of(artistSearch));
        given(artistInfoRepository.findByNicknameWithIdx("nickname", pageRequest))
            .willReturn(artistPage);

        // when
        var result = artistService.getArtistsByPage("nickname", pageRequest, null);

        // then
        assertAll(
            () -> assertThat(result.hasNext()).isFalse(),
            () -> assertThat(result.artists().size()).isEqualTo(1L),
            () -> assertThat(result.artists().getFirst().nickname()).isEqualTo("nickname"),
            () -> assertThat(result.artists().getFirst().artistImageUrl()).isEqualTo("artistImageUrl"),
            () -> assertThat(result.artists().getFirst().totalFollowers()).isEqualTo(10000L),
            () -> assertThat(result.artists().getFirst().totalLikes()).isEqualTo(10000L)
        );
    }

}
