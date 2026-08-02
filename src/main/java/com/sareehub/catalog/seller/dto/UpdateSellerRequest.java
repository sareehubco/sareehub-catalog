package com.sareehub.catalog.seller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateSellerRequest {
    @NotBlank private String businessName;
    private String bio;
    private String villageTown;
    private String cityDistrict;
    private String state;
    private String pincode;
    private String upiId;
}
