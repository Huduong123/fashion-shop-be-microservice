package io.github.Huduong123.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.Huduong123.order_service.dto.client.StockAdjustmentRequest;

// Gọi đến product-service thông qua API Gateway, dùng cho các API nội bộ
@FeignClient(name = "gateway-service", contextId = "productServiceClient", path = "/api/v1/internal/products")
public interface ProductServiceClient {

    @PostMapping("/adjust-stock")
    void adjustStock(@RequestBody StockAdjustmentRequest request);
}
