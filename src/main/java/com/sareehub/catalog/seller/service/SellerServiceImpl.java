package com.sareehub.catalog.seller.service;

import com.sareehub.catalog.product.entity.Product;
import com.sareehub.catalog.product.repository.ProductImageRepository;
import com.sareehub.catalog.product.repository.ProductRepository;
import com.sareehub.catalog.seller.dto.*;
import com.sareehub.catalog.seller.entity.SellerProfile;
import com.sareehub.catalog.seller.constant.VerificationStatus;
import com.sareehub.catalog.seller.repository.SellerProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerServiceImpl implements SellerService {

    private final SellerProfileRepository sellerRepo;
    private final ProductRepository productRepo;
    private final ProductImageRepository imageRepo;

    @Override
    public SellerResponse createSeller(CreateSellerRequest req) {
        SellerProfile seller = SellerProfile.builder()
                .userId(req.getUserId())
                .sellerType(req.getSellerType())
                .businessName(req.getBusinessName())
                .bio(req.getBio())
                .villageTown(req.getVillageTown())
                .cityDistrict(req.getCityDistrict())
                .state(req.getState())
                .pincode(req.getPincode())
                .upiId(req.getUpiId())
                .build();
        return toResponse(sellerRepo.save(seller), null);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerResponse getSellerWithListings(UUID sellerId) {
        SellerProfile seller = findSeller(sellerId);
        List<Product> products = productRepo.findBySeller(seller);
        return toResponse(seller, products);
    }

    @Override
    public SellerResponse updateProfile(UUID sellerId, UpdateSellerRequest req) {
        SellerProfile seller = findSeller(sellerId);
        seller.setBusinessName(req.getBusinessName());
        seller.setBio(req.getBio());
        seller.setVillageTown(req.getVillageTown());
        seller.setCityDistrict(req.getCityDistrict());
        seller.setState(req.getState());
        seller.setPincode(req.getPincode());
        seller.setUpiId(req.getUpiId());
        return toResponse(sellerRepo.save(seller), null);
    }

    @Override
    public SellerResponse updateVerificationStatus(UUID sellerId, UpdateVerificationStatusRequest req) {
        SellerProfile seller = findSeller(sellerId);
        seller.setVerificationStatus(req.getVerificationStatus());
        if (req.getVerificationStatus() == VerificationStatus.APPROVED) {
            seller.setVerifiedAt(LocalDateTime.now());
        }
        return toResponse(sellerRepo.save(seller), null);
    }

    private SellerProfile findSeller(UUID id) {
        return sellerRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found: " + id));
    }

    private SellerResponse toResponse(SellerProfile s, List<Product> products) {
        SellerResponse.SellerResponseBuilder b = SellerResponse.builder()
                .id(s.getId()).userId(s.getUserId()).sellerType(s.getSellerType())
                .businessName(s.getBusinessName()).bio(s.getBio())
                .villageTown(s.getVillageTown()).cityDistrict(s.getCityDistrict())
                .state(s.getState()).pincode(s.getPincode()).upiId(s.getUpiId())
                .verificationStatus(s.getVerificationStatus())
                .verifiedAt(s.getVerifiedAt()).createdAt(s.getCreatedAt());

        if (products != null) {
            b.products(products.stream().map(p -> {
                String primaryUrl = imageRepo.findByProductIdOrderByDisplayOrder(p.getId())
                        .stream().filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                        .map(img -> img.getImageUrl()).findFirst().orElse(null);
                return SellerResponse.ProductSummary.builder()
                        .id(p.getId()).title(p.getTitle()).price(p.getPrice())
                        .status(p.getStatus()).fabric(p.getFabric())
                        .weaveStyle(p.getWeaveStyle()).primaryImageUrl(primaryUrl)
                        .createdAt(p.getCreatedAt()).build();
            }).toList());
        }
        return b.build();
    }
}
