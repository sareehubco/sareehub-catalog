package com.sareehub.catalog.product.dto;

import com.sareehub.catalog.product.constant.MediaType;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data @Builder
public class ProductImageResponse {
    private UUID id;
    private String imageUrl;
    private MediaType mediaType;
    private Boolean isPrimary;
    private Integer displayOrder;
}
