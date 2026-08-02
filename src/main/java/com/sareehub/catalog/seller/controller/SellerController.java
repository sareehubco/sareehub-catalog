package com.sareehub.catalog.seller.controller;

import com.sareehub.catalog.product.dto.CreateProductRequest;
import com.sareehub.catalog.product.dto.ProductResponse;
import com.sareehub.catalog.product.service.ProductService;
import com.sareehub.catalog.seller.dto.*;
import com.sareehub.catalog.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final ProductService productService;

    // 1. POST /seller/sellers
    @PostMapping("/sellers")
    @ResponseStatus(HttpStatus.CREATED)
    public SellerResponse createSeller(@Valid @RequestBody CreateSellerRequest request) {
        return sellerService.createSeller(request);
    }

    // 2. GET /seller/{sellerId}
    @GetMapping("/{sellerId}")
    public SellerResponse getSellerWithListings(@PathVariable UUID sellerId) {
        return sellerService.getSellerWithListings(sellerId);
    }

    // 3. PUT /seller/{sellerId}/profile
    @PutMapping("/{sellerId}/profile")
    public SellerResponse updateProfile(
            @PathVariable UUID sellerId,
            @Valid @RequestBody UpdateSellerRequest request) {
        return sellerService.updateProfile(sellerId, request);
    }

    // 4. PATCH /seller/{sellerId}/verification-status
    @PatchMapping("/{sellerId}/verification-status")
    public SellerResponse updateVerificationStatus(
            @PathVariable UUID sellerId,
            @Valid @RequestBody UpdateVerificationStatusRequest request) {
        return sellerService.updateVerificationStatus(sellerId, request);
    }

    // 7. POST /seller/{sellerId}/products
    @PostMapping("/{sellerId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @PathVariable UUID sellerId,
            @Valid @RequestBody CreateProductRequest request) {
        return productService.createProduct(sellerId, request);
    }

    // 12. GET /seller/{sellerId}/products
    @GetMapping("/{sellerId}/products")
    public List<ProductResponse> getSellerProducts(@PathVariable UUID sellerId) {
        return productService.getSellerProducts(sellerId);
    }
}