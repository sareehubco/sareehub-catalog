package com.sareehub.catalog.product.dto;

import com.sareehub.catalog.product.constant.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    @NotBlank private String title;
    @NotNull  private WeaveStyle weaveStyle;
    @NotNull  private Fabric fabric;
    private String color;
    private String occasionTags;
    @NotNull @Positive private BigDecimal price;
    @NotNull @Min(0)   private Integer stockCount;
    private boolean blousePieceIncluded;
    @NotNull private CareInstruction careInstructions;
    private String description;
}
