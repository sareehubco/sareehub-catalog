package com.sareehub.catalog.seller.controller;

import com.sareehub.catalog.seller.dto.*;
import com.sareehub.catalog.seller.service.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    // 1. POST /sellers
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SellerResponse createSeller(@Valid @RequestBody CreateSellerRequest request) {
        return sellerService.createSeller(request);
    }

    // 2. GET /sellers/{sellerId}
    @GetMapping("/{sellerId}")
    public SellerResponse getSellerWithListings(@PathVariable UUID sellerId) {
        return sellerService.getSellerWithListings(sellerId);
    }

    // 3. PUT /sellers/{sellerId}/profile
    @PutMapping("/{sellerId}/profile")
    public SellerResponse updateProfile(
            @PathVariable UUID sellerId,
            @Valid @RequestBody UpdateSellerRequest request) {
        return sellerService.updateProfile(sellerId, request);
    }

    // 4. PATCH /sellers/{sellerId}/verification-status
    @PatchMapping("/{sellerId}/verification-status")
    public SellerResponse updateVerificationStatus(
            @PathVariable UUID sellerId,
            @Valid @RequestBody UpdateVerificationStatusRequest request) {
        return sellerService.updateVerificationStatus(sellerId, request);
    }
}
