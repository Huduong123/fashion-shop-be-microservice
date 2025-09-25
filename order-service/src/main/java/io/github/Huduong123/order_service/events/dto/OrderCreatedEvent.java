package io.github.Huduong123.order_service.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Event được phát khi có đơn hàng mới được tạo thành công.
 * Các service khác có thể lắng nghe để thực hiện các tác vụ bổ sung như:
 * - Gửi email xác nhận đơn hàng
 * - Xóa giỏ hàng của người dùng
 * - Cập nhật số liệu thống kê
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
