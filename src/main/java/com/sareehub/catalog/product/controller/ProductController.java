package com.sareehub.catalog.product.controller;

import com.sareehub.catalog.product.dto.*;
import com.sareehub.catalog.product.service.ProductImageService;
import com.sareehub.catalog.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService imageService;

    // 5. GET /product/products
    @GetMapping("/products")
    public Page<ProductResponse> browseProducts(
            @ModelAttribute ProductFilterRequest filter,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return productService.browseProducts(filter, PageRequest.of(page, size));
    }

    // 6. GET /product/{productId}
    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable UUID productId) {
        return productService.getProduct(productId);
    }

    // 8. PUT /product/{productId}
    @PutMapping("/{productId}")
    public ProductResponse updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequest request) {
        return productService.updateProduct(productId, request);
    }

    // 9. PATCH /product/{productId}/stock
    @PatchMapping("/{productId}/stock")
    public ProductResponse updateStock(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateStockRequest request) {
        return productService.updateStock(productId, request);
    }

    // 10. PATCH /product/{productId}/status
    @PatchMapping("/{productId}/status")
    public ProductResponse updateStatus(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateStatusRequest request) {
        return productService.updateStatus(productId, request);
    }

    // 11. DELETE /product/{productId}
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID productId) {
        productService.deleteProduct(productId);
    }

    // 13. POST /product/{productId}/images
    @PostMapping("/{productId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductImageResponse> addImages(
            @PathVariable UUID productId,
            @Valid @RequestBody List<AddImageRequest> requests) {
        return imageService.addImages(productId, requests);
    }

    // 14. DELETE /product/{productId}/images/{imageId}
    @DeleteMapping("/{productId}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {
        imageService.deleteImage(productId, imageId);
    }

    // 15. PATCH /product/{productId}/images/{imageId}/primary
    @PatchMapping("/{productId}/images/{imageId}/primary")
    public ProductImageResponse setPrimaryImage(
            @PathVariable UUID productId,
            @PathVariable UUID imageId) {
        return imageService.setPrimary(productId, imageId);
    }
}