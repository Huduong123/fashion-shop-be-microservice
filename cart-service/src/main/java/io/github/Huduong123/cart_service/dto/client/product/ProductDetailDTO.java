package io.github.Huduong123.cart_service.dto.client.product;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductDetailDTO {
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