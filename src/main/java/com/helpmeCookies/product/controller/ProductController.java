package com.helpmeCookies.product.controller;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.ApiResponse.SuccessCode;
import com.helpmeCookies.global.jwt.JwtUser;
import com.helpmeCookies.product.dto.ImageUpload;
import static com.helpmeCookies.product.util.SortUtil.convertProductSort;

import com.helpmeCookies.product.controller.docs.ProductApiDocs;
import com.helpmeCookies.product.dto.ProductImageResponse;
import com.helpmeCookies.product.dto.ProductPage;
import com.helpmeCookies.product.dto.ProductPage.Paging;
import com.helpmeCookies.product.dto.ProductRequest;
import com.helpmeCookies.product.dto.ProductResponse;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.service.ProductImageService;
import com.helpmeCookies.product.service.ProductLikeService;
import com.helpmeCookies.product.service.ProductService;
import com.helpmeCookies.product.util.ProductSort;
import com.helpmeCookies.review.dto.ReviewResponse;
import com.helpmeCookies.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController implements ProductApiDocs {

    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ReviewService reviewService;
    private final ProductLikeService productLikeService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.save(productRequest);
        productImageService.saveImages(product.getId(),productRequest.imageUrls());
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }

    @PostMapping("/images")
    public ResponseEntity<ApiResponse<ProductImageResponse>> uploadImages(List<MultipartFile> files) {
        List<ImageUpload> responses = productImageService.uploadMultiFiles(files);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK,new ProductImageResponse(responses.stream().map(ImageUpload::photoUrl).toList())));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductInfo(@PathVariable("productId") Long productId) {
        Product product = productService.find(productId);
        List<String> urls = productImageService.getImages(productId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK,ProductResponse.from(product,urls)));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> editProductInfo(@PathVariable("productId") Long productId,
                                                           @RequestBody ProductRequest productRequest) {
        productService.edit(productId, productRequest);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }

    @PutMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<Void>> editImages(@PathVariable("productId") Long productId, List<MultipartFile> files) {
        productImageService.editImages(productId, files);
        List<String> images = productImageService.uploadMultiFiles(files).stream()
                .map(ImageUpload::photoUrl).toList();
        productService.editThumbnailImage(productId,images);
        productImageService.saveImages(productId,images);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.NO_CONTENT));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ProductPage.Paging>> getProductsByPage(
        @RequestParam("query") String query,
        @RequestParam(name = "size", required = false, defaultValue = "20") int size,
        @RequestParam("page") int page,
        @RequestParam("sort") ProductSort productSort
    ) {
        var sort = convertProductSort(productSort);
        var pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK,productService.getProductsByPage(query, pageable)));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<ProductPage.Paging>> getProductsWithRandomPaging(
        @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(0, size);
      
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK, productService.getProductsWithRandomPaging(pageable)));
    }

    @GetMapping("/{productId}/reviews")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllReviewsByProduct(
            @PathVariable("productId") Long productId,
            @PageableDefault(size = 7) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK,reviewService.getAllReviewByProduct(productId,pageable)));
    }

    @PostMapping("/{productId}/likes")
    public ResponseEntity<ApiResponse<Void>> postProductLike(
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal JwtUser jwtUser) {
        productLikeService.productLike(jwtUser.getId(), productId);
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }

    @DeleteMapping("/{productId}/likes")
    public ResponseEntity<ApiResponse<Void>> deleteProductlike(
            @PathVariable("productId") Long productId,
            @AuthenticationPrincipal JwtUser jwtUser) {
        productLikeService.deleteProductLike(jwtUser.getId(), productId);

        return ResponseEntity.ok(ApiResponse.success(SuccessCode.NO_CONTENT));
    }
}
