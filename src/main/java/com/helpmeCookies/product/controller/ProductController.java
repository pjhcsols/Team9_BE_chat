package com.helpmeCookies.product.controller;

import com.helpmeCookies.global.ApiResponse.ApiResponse;
import com.helpmeCookies.global.ApiResponse.SuccessCode;
import com.helpmeCookies.product.dto.ImageUpload;
import static com.helpmeCookies.product.util.SortUtil.convertProductSort;

import com.helpmeCookies.product.controller.docs.ProductApiDocs;
import com.helpmeCookies.product.dto.ProductImageResponse;
import com.helpmeCookies.product.dto.ProductPage;
import com.helpmeCookies.product.dto.ProductRequest;
import com.helpmeCookies.product.dto.ProductResponse;
import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.product.service.ProductImageService;
import com.helpmeCookies.product.service.ProductService;
import com.helpmeCookies.product.util.ProductSort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController implements ProductApiDocs {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @PostMapping("/successTest")
    public ResponseEntity<ApiResponse<Void>> saveTest() {
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.OK));
    }

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

    @GetMapping("/feed")
    public ResponseEntity<ProductPage.Paging> getProductsWithRandomPaging(
        @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(0, size);
        return ResponseEntity.ok(productService.getProductsWithRandomPaging(pageable));
    }
}
