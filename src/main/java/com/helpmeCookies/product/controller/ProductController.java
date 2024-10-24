package com.helpmeCookies.product.controller;

import com.helpmeCookies.product.dto.FileUploadResponse;
import com.helpmeCookies.product.dto.ProductImageResponse;
import static com.helpmeCookies.product.util.SortUtil.convertProductSort;

import com.helpmeCookies.product.dto.ProductPage;
import com.helpmeCookies.product.dto.ProductRequest;
import com.helpmeCookies.product.dto.ProductResponse;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.service.ProductImageService;
import com.helpmeCookies.product.service.ProductService;
import com.helpmeCookies.product.util.ProductSort;
import lombok.RequiredArgsConstructor;
import com.helpmeCookies.product.dto.ProductPage;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<Void> saveProduct(@RequestBody ProductRequest productRequest) {
        productService.save(productRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<ProductImageResponse> uploadImages(@PathVariable("productId") Long productId, List<MultipartFile> files) throws IOException {
        List<FileUploadResponse> responses = productImageService.uploadMultiFiles(productId,files);
        return ResponseEntity.ok(new ProductImageResponse(responses.stream().map(FileUploadResponse::photoUrl).toList()));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductInfo(@PathVariable("productId") Long productId) {
        Product product = productService.find(productId);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Void> editProductInfo(@PathVariable("productId") Long productId,
                                                           @RequestBody ProductRequest productRequest) {
        productService.edit(productId, productRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productId}/images")
    public ResponseEntity<Void> editImages(@PathVariable("productId") Long productId, List<MultipartFile> files) throws IOException {
        productImageService.editImages(productId, files);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ProductPage.Paging> getProductsByPage(
        @RequestParam("query") String query,
        @RequestParam(name = "size", required = false, defaultValue = "20") int size,
        @RequestParam("page") int page,
        @RequestParam("sort") ProductSort productSort
    ) {
        var sort = convertProductSort(productSort);
        var pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(productService.getProductsByPage(query, pageable));
    }
}
