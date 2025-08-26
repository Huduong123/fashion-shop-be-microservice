package io.github.Huduong123.product_service.service.user;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.Huduong123.product_service.dto.admin.product.ProductResponseDTO;


public interface UserProductService {

    Page<ProductResponseDTO> findAllVisibleProducts(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Long categoryId,
            Pageable pageable);

    ProductResponseDTO findVisibleProductById(Long productId);

    Page<ProductResponseDTO> findVisibleProductsByCategory(Long categoryId, Pageable pageable);
}
