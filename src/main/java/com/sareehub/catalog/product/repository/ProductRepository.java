package com.sareehub.catalog.product.repository;

import com.sareehub.catalog.product.entity.Product;
import com.sareehub.catalog.product.constant.ProductStatus;
import com.sareehub.catalog.seller.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    List<Product> findBySeller(SellerProfile seller);
    List<Product> findBySellerAndStatus(SellerProfile seller, ProductStatus status);
}
