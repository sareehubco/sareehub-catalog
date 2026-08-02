package com.sareehub.catalog.seller.service;

import com.sareehub.catalog.seller.dto.*;
import java.util.UUID;

public interface SellerService {
    SellerResponse createSeller(CreateSellerRequest request);
    SellerResponse getSellerWithListings(UUID sellerId);
    SellerResponse updateProfile(UUID sellerId, UpdateSellerRequest request);
    SellerResponse updateVerificationStatus(UUID sellerId, UpdateVerificationStatusRequest request);
}
