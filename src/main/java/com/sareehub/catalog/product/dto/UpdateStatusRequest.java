package com.sareehub.catalog.product.dto;

import com.sareehub.catalog.product.constant.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    @NotNull private ProductStatus status;
}
