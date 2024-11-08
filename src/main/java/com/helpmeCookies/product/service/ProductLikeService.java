package com.helpmeCookies.product.service;

import com.helpmeCookies.product.entity.Like;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.repository.ProductLikeRepository;
import com.helpmeCookies.product.repository.ProductRepository;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductLikeService {
    private final ProductLikeRepository productLikeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void productLike(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저Id입니다." + userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품Id입니다." + productId));
        Like like = new Like(user, product);
        productLikeRepository.save(like);
    }

    @Transactional
    public void deleteProductLike(Long userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저Id입니다." + userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품Id입니다." + productId));

        Like like = productLikeRepository.findDistinctFirstByUserAndProduct(user,product).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 찜 항목입니다."));
        productLikeRepository.delete(like);
    }
}
