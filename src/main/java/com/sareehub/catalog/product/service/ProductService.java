package com.sareehub.catalog.product.service;

import com.sareehub.catalog.product.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductResponse> browseProducts(ProductFilterRequest filter, Pageable pageable);
    ProductResponse getProduct(UUID productId);
    ProductResponse createProduct(UUID sellerId, CreateProductRequest request);
    ProductResponse updateProduct(UUID productId, UpdateProductRequest request);
    ProductResponse updateStock(UUID productId, UpdateStockRequest request);
    ProductResponse updateStatus(UUID productId, UpdateStatusRequest request);
    void deleteProduct(UUID productId);
    List<ProductResponse> getSellerProducts(UUID sellerId);
}
