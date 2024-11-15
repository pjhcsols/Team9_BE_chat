package com.helpmeCookies.product.controller;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.ApiResponse.SuccessCode;
import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.product.dto.ProductPage.Paging;
import com.helpmeCookies.product.service.ProductLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/wishes")
@RequiredArgsConstructor
public class WishController {
    private ProductLikeService productLikeService;

    @GetMapping
    public ResponseEntity<ApiResponse<Paging>> getAllMyLikeProducts(
            @AuthenticationPrincipal JwtUser jwtUser, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK,productLikeService.getLikeProducts(jwtUser.getId(),pageable)));
    }
}
