package io.github.Huduong123.product_service.service.admin;


import java.util.List;

import io.github.Huduong123.product_service.dto.admin.product.ProductVariantSizeCreateDTO;
import io.github.Huduong123.product_service.dto.admin.product.ProductVariantSizeResponseDTO;
import io.github.Huduong123.product_service.dto.admin.product.ProductVariantSizeUpdateDTO;
import io.github.Huduong123.product_service.dto.common.ResponseMessageDTO;

public interface ProductVariantSizeService {

    /**
     * Get all sizes for a specific product variant
     */
    List<ProductVariantSizeResponseDTO> getSizesByProductVariantId(Long productVariantId);

    /**
     * Create a new size for a product variant
     */
    ProductVariantSizeResponseDTO createSize(Long productVariantId, ProductVariantSizeCreateDTO createDTO);

    /**
     * Update an existing product variant size
     */
    ProductVariantSizeResponseDTO updateSize(Long id, ProductVariantSizeUpdateDTO updateDTO);

    /**
     * Delete a product variant size
     */
    ResponseMessageDTO deleteSize(Long id);

    /**
     * Get a specific size by ID
     */
    ProductVariantSizeResponseDTO getSizeById(Long id);

    /**
     * Check if a size exists for a product variant
     */
    boolean existsByProductVariantIdAndSizeId(Long productVariantId, Long sizeId);
}