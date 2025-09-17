package io.github.Huduong123.product_service.dto.internal;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InternalProductDetailDTO {
    private Long productId;
    private Long productVariantId;
    private Long sizeId;
    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;
    private BigDecimal price;
    private Integer stock; // Số lượng tồn kho
}