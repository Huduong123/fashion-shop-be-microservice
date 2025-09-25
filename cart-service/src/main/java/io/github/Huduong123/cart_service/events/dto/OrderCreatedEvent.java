package io.github.Huduong123.cart_service.events.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event được nhận khi có đơn hàng mới được tạo thành công từ Order Service.
 * Cart Service sẽ lắng nghe event này để xóa giỏ hàng của người dùng.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long orderId;
    private String orderCode;
}
