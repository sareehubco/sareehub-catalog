package com.sareehub.catalog.product.dto;

import com.sareehub.catalog.product.constant.Fabric;
import com.sareehub.catalog.product.constant.WeaveStyle;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductFilterRequest {
    private Fabric fabric;
    private WeaveStyle weaveStyle;
    private String color;
    private String occasionTags;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String sort;
}
