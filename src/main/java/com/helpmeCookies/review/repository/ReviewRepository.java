package com.helpmeCookies.review.repository;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByProduct(Product product);
}
