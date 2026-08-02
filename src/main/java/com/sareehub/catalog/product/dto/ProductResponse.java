package com.sareehub.catalog.product.dto;

import com.sareehub.catalog.product.constant.*;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data @Builder
public class ProductResponse {
    private UUID id;
    private UUID sellerId;
    private String sellerBusinessName;
    private String title;
    private WeaveStyle weaveStyle;
    private Fabric fabric;
    private String color;
    private String occasionTags;
    private BigDecimal price;
    private Integer stockCount;
    private Boolean blousePieceIncluded;
    private CareInstruction careInstructions;
    private String description;
    private ProductStatus status;
    private List<ProductImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
