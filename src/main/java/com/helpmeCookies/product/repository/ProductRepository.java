package com.helpmeCookies.product.repository;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.repository.dto.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.id, p.name, p.thumbnail_url, a.nickname AS artist, p.price " +
        "FROM product p JOIN artist_info a ON p.artist_info_id = a.id " +
        "WHERE MATCH(p.name) AGAINST (:query IN BOOLEAN MODE)",
        countQuery = "SELECT COUNT(*) " +
            "FROM product p JOIN artist_info a ON p.artist_info_id = a.id " +
            "WHERE MATCH(p.name) AGAINST (:query IN BOOLEAN MODE)",
        nativeQuery = true) // Index 사용
    Page<ProductSearch> findByNameWithIdx(@Param("query") String query, Pageable pageable);
}
