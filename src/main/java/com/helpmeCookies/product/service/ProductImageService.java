package com.helpmeCookies.product.service;

import com.helpmeCookies.global.utils.AwsS3FileUtils;
import com.helpmeCookies.product.dto.ImageUpload;
import com.helpmeCookies.product.entity.ProductImage;
import com.helpmeCookies.product.repository.ProductImageRepository;
import java.util.ArrayList;
import com.helpmeCookies.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {
    private final AwsS3FileUtils awsS3FileUtils;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Transactional
    public List<ImageUpload> uploadMultiFiles(List<MultipartFile> files) {
        List<ImageUpload> imageUploads = new ArrayList<>();
        for (MultipartFile multipartFile:files) {
            imageUploads.add(awsS3FileUtils.uploadMultiImages(multipartFile));
        }
        return imageUploads;
    }

    @Transactional
    public void saveImages(Long productId,List<String> urls) {
        //DTO 변환
        List<ImageUpload> files = urls.stream().map(ImageUpload::new).toList();
        files.forEach(image -> productImageRepository.save(image.toEntity(productId)));
    }

    @Transactional
    public void editImages(Long productId, List<MultipartFile> files) {
        //우선은 전부 삭제하고 다시 업로드
        //추후에 개선 예정
        //TODO s3서버에서 기존 사진들을 제거하는 기능
        productImageRepository.deleteAllByProductId(productId);
    }

    public List<String> getImages(Long productId) {
        return productImageRepository.findAllByProductId(productId).stream().map(ProductImage::getPhotoUrl).toList();
    }
}
