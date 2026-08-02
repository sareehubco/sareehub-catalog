package com.sareehub.catalog.product.service;

import com.sareehub.catalog.product.dto.AddImageRequest;
import com.sareehub.catalog.product.dto.ProductImageResponse;
import com.sareehub.catalog.product.entity.Product;
import com.sareehub.catalog.product.entity.ProductImage;
import com.sareehub.catalog.product.repository.ProductImageRepository;
import com.sareehub.catalog.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepo;
    private final ProductImageRepository imageRepo;

    @Override
    public List<ProductImageResponse> addImages(UUID productId, List<AddImageRequest> requests) {
        Product product = findProduct(productId);
        List<ProductImage> saved = requests.stream().map(req ->
                imageRepo.save(ProductImage.builder()
                        .product(product)
                        .imageUrl(req.getImageUrl())
                        .mediaType(req.getMediaType())
                        .isPrimary(false)
                        .displayOrder(req.getDisplayOrder())
                        .build())
        ).toList();
        return saved.stream().map(this::toResponse).toList();
    }

    @Override
    public void deleteImage(UUID productId, UUID imageId) {
        ProductImage image = findImage(productId, imageId);
        imageRepo.delete(image);
    }

    @Override
    public ProductImageResponse setPrimary(UUID productId, UUID imageId) {
        findProduct(productId);
        List<ProductImage> all = imageRepo.findByProductIdOrderByDisplayOrder(productId);
        all.forEach(img -> img.setIsPrimary(img.getId().equals(imageId)));
        imageRepo.saveAll(all);
        return all.stream().filter(img -> img.getId().equals(imageId))
                .findFirst().map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + imageId));
    }

    private Product findProduct(UUID id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    private ProductImage findImage(UUID productId, UUID imageId) {
        return imageRepo.findById(imageId).filter(img -> img.getProduct().getId().equals(productId))
                .orElseThrow(() -> new EntityNotFoundException("Image not found: " + imageId));
    }

    private ProductImageResponse toResponse(ProductImage img) {
        return ProductImageResponse.builder()
                .id(img.getId()).imageUrl(img.getImageUrl())
                .mediaType(img.getMediaType()).isPrimary(img.getIsPrimary())
                .displayOrder(img.getDisplayOrder()).build();
    }
}
