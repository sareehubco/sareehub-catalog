package com.sareehub.catalog.product.service;

import com.sareehub.catalog.product.constant.ProductStatus;
import com.sareehub.catalog.product.dto.*;
import com.sareehub.catalog.product.entity.Product;
import com.sareehub.catalog.product.repository.ProductImageRepository;
import com.sareehub.catalog.product.repository.ProductRepository;
import com.sareehub.catalog.seller.entity.SellerProfile;
import com.sareehub.catalog.seller.constant.VerificationStatus;
import com.sareehub.catalog.seller.repository.SellerProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ProductImageRepository imageRepo;
    private final SellerProfileRepository sellerRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> browseProducts(ProductFilterRequest filter, Pageable pageable) {
        Specification<Product> spec = buildSpec(filter);
        return productRepo.findAll(spec, pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID productId) {
        return toResponse(findProduct(productId));
    }

    @Override
    public ProductResponse createProduct(UUID sellerId, CreateProductRequest req) {
        SellerProfile seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found: " + sellerId));

        if (seller.getVerificationStatus() != VerificationStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Seller must be approved before creating a listing");
        }

        ProductStatus initialStatus = req.getStockCount() == 0
                ? ProductStatus.SOLD_OUT : ProductStatus.DRAFT;

        Product product = Product.builder()
                .seller(seller)
                .title(req.getTitle())
                .weaveStyle(req.getWeaveStyle())
                .fabric(req.getFabric())
                .color(req.getColor())
                .occasionTags(req.getOccasionTags())
                .price(req.getPrice())
                .stockCount(req.getStockCount())
                .blousePieceIncluded(req.isBlousePieceIncluded())
                .careInstructions(req.getCareInstructions())
                .description(req.getDescription())
                .status(initialStatus)
                .build();
        return toResponse(productRepo.save(product));
    }

    @Override
    public ProductResponse updateProduct(UUID productId, UpdateProductRequest req) {
        Product p = findProduct(productId);
        p.setTitle(req.getTitle());
        p.setWeaveStyle(req.getWeaveStyle());
        p.setFabric(req.getFabric());
        p.setColor(req.getColor());
        p.setOccasionTags(req.getOccasionTags());
        p.setPrice(req.getPrice());
        p.setBlousePieceIncluded(req.isBlousePieceIncluded());
        p.setCareInstructions(req.getCareInstructions());
        p.setDescription(req.getDescription());
        applyStockUpdate(p, req.getStockCount());
        return toResponse(productRepo.save(p));
    }

    @Override
    public ProductResponse updateStock(UUID productId, UpdateStockRequest req) {
        Product p = findProduct(productId);
        applyStockUpdate(p, req.getStockCount());
        return toResponse(productRepo.save(p));
    }

    @Override
    public ProductResponse updateStatus(UUID productId, UpdateStatusRequest req) {
        Product p = findProduct(productId);
        if (req.getStatus() == ProductStatus.ACTIVE) {
            boolean hasImage = imageRepo.findByProductIdOrderByDisplayOrder(productId).stream().findAny().isPresent();
            if (!hasImage) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "Product needs at least one image before going active");
            }
        }
        p.setStatus(req.getStatus());
        return toResponse(productRepo.save(p));
    }

    @Override
    public void deleteProduct(UUID productId) {
        productRepo.delete(findProduct(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getSellerProducts(UUID sellerId) {
        SellerProfile seller = sellerRepo.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found: " + sellerId));
        return productRepo.findBySeller(seller).stream().map(this::toResponse).toList();
    }

    private void applyStockUpdate(Product p, int newStock) {
        p.setStockCount(newStock);
        if (newStock == 0) p.setStatus(ProductStatus.SOLD_OUT);
    }

    private Product findProduct(UUID id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    private ProductResponse toResponse(Product p) {
        List<ProductImageResponse> images = imageRepo
                .findByProductIdOrderByDisplayOrder(p.getId())
                .stream().map(img -> ProductImageResponse.builder()
                        .id(img.getId()).imageUrl(img.getImageUrl())
                        .mediaType(img.getMediaType()).isPrimary(img.getIsPrimary())
                        .displayOrder(img.getDisplayOrder()).build())
                .toList();

        return ProductResponse.builder()
                .id(p.getId())
                .sellerId(p.getSeller().getId())
                .sellerBusinessName(p.getSeller().getBusinessName())
                .title(p.getTitle()).weaveStyle(p.getWeaveStyle())
                .fabric(p.getFabric()).color(p.getColor())
                .occasionTags(p.getOccasionTags()).price(p.getPrice())
                .stockCount(p.getStockCount())
                .blousePieceIncluded(p.getBlousePieceIncluded())
                .careInstructions(p.getCareInstructions())
                .description(p.getDescription()).status(p.getStatus())
                .images(images).createdAt(p.getCreatedAt()).updatedAt(p.getUpdatedAt())
                .build();
    }

    private Specification<Product> buildSpec(ProductFilterRequest f) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<>();
            predicates.add(cb.equal(root.get("status"), ProductStatus.ACTIVE));
            if (f.getFabric()       != null) predicates.add(cb.equal(root.get("fabric"), f.getFabric()));
            if (f.getWeaveStyle()   != null) predicates.add(cb.equal(root.get("weaveStyle"), f.getWeaveStyle()));
            if (f.getColor()        != null) predicates.add(cb.like(cb.lower(root.get("color")), "%" + f.getColor().toLowerCase() + "%"));
            if (f.getOccasionTags() != null) predicates.add(cb.like(root.get("occasionTags"), "%" + f.getOccasionTags() + "%"));
            if (f.getMinPrice()     != null) predicates.add(cb.greaterThanOrEqualTo(root.get("price"), f.getMinPrice()));
            if (f.getMaxPrice()     != null) predicates.add(cb.lessThanOrEqualTo(root.get("price"), f.getMaxPrice()));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
