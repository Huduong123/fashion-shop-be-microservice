package io.github.Huduong123.order_service.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Event được phát khi đơn hàng bị hủy.
 * Product Service sẽ lắng nghe để hoàn lại số lượng tồn kho.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancelledEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private String orderCode;
    private List<OrderItemPayload> itemsToRestore;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemPayload implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long productVariantId;
        private Long sizeId;
        private int quantity; // Số lượng cần hoàn lại
    }
}
