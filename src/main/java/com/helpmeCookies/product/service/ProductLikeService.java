package com.helpmeCookies.product.service;

import com.helpmeCookies.product.dto.ProductPage;
import com.helpmeCookies.product.entity.Like;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.repository.ProductLikeRepository;
import com.helpmeCookies.product.repository.ProductRepository;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        User user = getUser(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품Id입니다." + productId));
        Like like = new Like(user, product);
        productLikeRepository.save(like);
    }

    @Transactional(readOnly = true)
    public ProductPage.Paging getLikeProducts(Long userId, Pageable pageable) {
        User user = getUser(userId);
        Page<Product> productPage = productLikeRepository.findAllByUser(user,pageable).map(Like::getProduct);
        return ProductPage.Paging.fromProduct(productPage);
    }

    @Transactional
    public void deleteProductLike(Long userId, Long productId) {
        User user = getUser(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품Id입니다." + productId));

        Like like = productLikeRepository.findDistinctFirstByUserAndProduct(user,product).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 찜 항목입니다."));
        productLikeRepository.delete(like);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저Id입니다." + userId));
    }
}
