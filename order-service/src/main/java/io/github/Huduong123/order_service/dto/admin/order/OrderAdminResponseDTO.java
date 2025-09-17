package io.github.Huduong123.order_service.dto.admin.order;

import io.github.Huduong123.order_service.entity.enums.OrderStatus;
import io.github.Huduong123.order_service.entity.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderAdminResponseDTO {
    // --- Dữ liệu Order Service sở hữu ---
    private Long id;
    private String orderCode;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int totalItems;
    
    // --- Thông tin thanh toán (Order Service sở hữu) ---
    private String paymentMethodCode;
    private String paymentMethodName;

    // --- ID tham chiếu đến User Service ---
    private Long userId; // Chỉ trả về ID, không trả về chi tiết người dùng

    // --- Chi tiết đơn hàng (Order Service sở hữu) ---
    private OrderShippingDetailDTO shippingDetail;
    private List<OrderItemAdminResponseDTO> orderItems;
}