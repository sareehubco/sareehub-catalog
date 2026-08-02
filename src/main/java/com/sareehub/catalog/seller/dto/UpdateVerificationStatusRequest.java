package com.sareehub.catalog.seller.dto;

import com.sareehub.catalog.seller.constant.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateVerificationStatusRequest {
    @NotNull private VerificationStatus verificationStatus;
}
