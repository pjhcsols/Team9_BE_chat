package com.helpmeCookies.product.repository;

import com.helpmeCookies.product.entity.Like;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findDistinctFirstByUserAndProduct(User user, Product product);
}
