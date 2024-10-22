package com.helpmeCookies.product.controller;

import com.helpmeCookies.product.dto.ImageUpload;
import com.helpmeCookies.product.dto.ProductImageResponse;
import com.helpmeCookies.product.dto.ProductRequest;
import com.helpmeCookies.product.dto.ProductResponse;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.service.ProductImageService;
import com.helpmeCookies.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<Void> saveProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.save(productRequest);
        productImageService.saveImages(product.getId(),productRequest.imageUrls());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/images")
    public ResponseEntity<ProductImageResponse> uploadImages(List<MultipartFile> files) {
        List<ImageUpload> responses = productImageService.uploadMultiFiles(files);
        return ResponseEntity.ok(new ProductImageResponse(responses.stream().map(ImageUpload::photoUrl).toList()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductInfo(@PathVariable("productId") Long productId) {
        Product product = productService.find(productId);
        List<String> urls = productImageService.getImages(productId);
        return ResponseEntity.ok(ProductResponse.from(product,urls));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> editProductInfo(@PathVariable("productId") Long productId,
                                                           @RequestBody ProductRequest productRequest) {
        productService.edit(productId, productRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/images")
    public ResponseEntity<Void> editImages(@PathVariable("productId") Long productId, List<MultipartFile> files) {
        productImageService.editImages(productId, files);
        List<String> images = productImageService.uploadMultiFiles(files).stream()
                .map(ImageUpload::photoUrl).toList();
        productImageService.saveImages(productId,images);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
