package com.sareehub.catalog.seller.dto;

import com.sareehub.catalog.seller.constant.SellerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateSellerRequest {
    @NotNull  private UUID userId;
    @NotNull  private SellerType sellerType;
    @NotBlank private String businessName;
    private String bio;
    private String villageTown;
    private String cityDistrict;
    private String state;
    private String pincode;
    private String upiId;
}
