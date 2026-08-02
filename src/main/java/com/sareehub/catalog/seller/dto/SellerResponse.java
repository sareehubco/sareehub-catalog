package com.sareehub.catalog.seller.dto;

import com.sareehub.catalog.product.constant.Fabric;
import com.sareehub.catalog.product.constant.ProductStatus;
import com.sareehub.catalog.product.constant.WeaveStyle;
import com.sareehub.catalog.seller.constant.SellerType;
import com.sareehub.catalog.seller.constant.VerificationStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data @Builder
public class SellerResponse {
    private UUID id;
    private UUID userId;
    private SellerType sellerType;
    private String businessName;
    private String bio;
    private String villageTown;
    private String cityDistrict;
    private String state;
    private String pincode;
    private String upiId;
    private VerificationStatus verificationStatus;
    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;
    private List<ProductSummary> products;

    @Data @Builder
    public static class ProductSummary {
        private UUID id;
        private String title;
        private BigDecimal price;
        private ProductStatus status;
        private Fabric fabric;
        private WeaveStyle weaveStyle;
        private String primaryImageUrl;
        private LocalDateTime createdAt;
    }
}
