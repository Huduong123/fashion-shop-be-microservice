package io.github.Huduong123.order_service.dto.user.order;

import io.github.Huduong123.order_service.entity.enums.OrderStatus;
import io.github.Huduong123.order_service.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderSummaryDTO {
    private Long id;
    private String orderCode;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private int totalItems; // Tổng số lượng sản phẩm (ví dụ: 2 áo + 1 quần = 3)
    private String representativeImage; // URL ảnh của sản phẩm đầu tiên để hiển thị
}