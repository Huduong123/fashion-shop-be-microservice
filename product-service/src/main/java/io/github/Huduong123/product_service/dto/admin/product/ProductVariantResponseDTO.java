package io.github.Huduong123.product_service.dto.admin.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.github.Huduong123.product_service.entity.enums.ProductVariantStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductVariantResponseDTO {

    private Long id;

    private Long colorId;
    private String colorName;

    private List<ProductVariantSizeResponseDTO> sizes = new ArrayList<>();

    // Calculated fields for convenience
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer totalQuantity;
    private boolean available; // Has at least one available size

    private String imageUrl; // Keep for backward compatibility
    private ProductVariantStatus status;

    // List of variant images
    private List<ProductVariantImageDTO> images = new ArrayList<>();

}
