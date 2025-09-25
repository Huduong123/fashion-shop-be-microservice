package io.github.Huduong123.order_service.events.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeletedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;

    private List<OrderItemPayload> itemsToRestore;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemPayload implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long productVariantId;
        private Long sizeId;
        private int quantity;
    }
}
