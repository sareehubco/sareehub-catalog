package com.sareehub.catalog.product.dto;

import com.sareehub.catalog.product.constant.MediaType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddImageRequest {
    @NotBlank private String imageUrl;
    @NotNull  private MediaType mediaType;
    @NotNull @Min(0) private Integer displayOrder;
}
