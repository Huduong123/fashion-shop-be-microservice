package io.github.Huduong123.order_service.dto.user.order;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponseDTO {
    private Long productId;
    private Long productVariantId;
    private Long sizeId;
    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;
    private int quantity;
    private BigDecimal price; // Giá tại thời điểm mua
    private BigDecimal subTotal; // Thành tiền (price * quantity)
}