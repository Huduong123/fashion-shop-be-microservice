package io.github.Huduong123.order_service.dto.client;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartDetailDTO {
    private List<CartItemDTO> cartItems;

    @Data
    public static class CartItemDTO {
        private Long productId;
        private Long productVariantId;
        private Long sizeId;
        private String productName;
        private String colorName;
        private String sizeName;
        private String imageUrl;
        private BigDecimal price;
        private Integer quantity;
    }
}