package io.github.Huduong123.order_service.dto.user.order;

import io.github.Huduong123.order_service.entity.enums.OrderStatus;
import io.github.Huduong123.order_service.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponseDTO {
    private Long id;
    private String orderCode; // Mã đơn hàng thân thiện với người dùng
    private BigDecimal totalPrice;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private String note;

    // --- Dữ liệu snapshot ---
    private String paymentMethodName;
    private String paymentMethodCode;
    private OrderShippingDetailResponseDTO shippingDetail;
    
    // --- Chi tiết các sản phẩm ---
    private List<OrderItemResponseDTO> orderItems;
}