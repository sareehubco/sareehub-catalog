package com.sareehub.catalog.product.service;

import com.sareehub.catalog.product.dto.AddImageRequest;
import com.sareehub.catalog.product.dto.ProductImageResponse;

import java.util.List;
import java.util.UUID;

public interface ProductImageService {
    List<ProductImageResponse> addImages(UUID productId, List<AddImageRequest> requests);
    void deleteImage(UUID productId, UUID imageId);
    ProductImageResponse setPrimary(UUID productId, UUID imageId);
}
