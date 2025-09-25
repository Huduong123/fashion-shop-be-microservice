package io.github.Huduong123.product_service.dto.internal;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockAdjustmentRequest {
    private List<StockItem> items;

    @Data
    @Builder
    public static class StockItem {
        private Long productVariantId;
        private Long sizeId;
        private int quantity; // Số lượng cần trừ (số âm để hoàn kho)
    }
}
