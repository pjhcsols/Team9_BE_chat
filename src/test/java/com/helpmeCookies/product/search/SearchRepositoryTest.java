package com.helpmeCookies.product.search;

import static com.helpmeCookies.product.util.SortUtil.convertProductSort;
import static org.assertj.core.api.Assertions.assertThat;

import com.helpmeCookies.global.config.QueryDSLConfig;
import com.helpmeCookies.product.repository.ProductRepository;
import com.helpmeCookies.product.util.ProductSort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@ExtendWith(OutputCaptureExtension.class)
@Import(QueryDSLConfig.class)
public class SearchRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("상품 검색 쿼리 확인")
    void 상품_검색(CapturedOutput out) {
        // given
        var sort = convertProductSort(ProductSort.LATEST);
        var pageRequest = PageRequest.of(0, 10, sort);

        // when
        try{
            productRepository.findByNameWithIdx("product", pageRequest);
        } catch (Exception ignored) {
        }

        // then
        assertThat(out.getOut())
            .contains("SELECT")
            .contains("p.id,")
            .contains("p.name,")
            .contains("a.nickname AS artist,")
            .contains("p.price")
            .contains("FROM product p")
            .contains("JOIN artist_info a ON p.artist_info_id = a.id")
            .contains("WHERE MATCH(p.name) AGAINST (? IN BOOLEAN MODE)")
            .contains("order by p.created_date desc");
        //.contains("limit ?") 테스트 dbh2 db에서는 limit이 없어서 제외
   }
}
