package com.sareehub.catalog.product.entity;

import com.sareehub.catalog.product.constant.*;
import com.sareehub.catalog.seller.entity.SellerProfile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private SellerProfile seller;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "weave_style", nullable = false)
    private WeaveStyle weaveStyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Fabric fabric;

    private String color;

    @Column(name = "occasion_tags")
    private String occasionTags;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_count", nullable = false)
    private Integer stockCount;

    @Column(name = "blouse_piece_included", nullable = false)
    @Builder.Default
    private Boolean blousePieceIncluded = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "care_instructions", nullable = false)
    private CareInstruction careInstructions;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
