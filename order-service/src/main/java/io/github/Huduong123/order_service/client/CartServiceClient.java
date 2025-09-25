package io.github.Huduong123.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import io.github.Huduong123.order_service.dto.client.CartDetailDTO;

// Gọi đến cart-service thông qua API Gateway
@FeignClient(name = "gateway-service", contextId = "cartServiceClient", path = "/api/v1/cart")
public interface CartServiceClient {

    @GetMapping
    CartDetailDTO getCartDetails(@RequestHeader("Authorization") String token);
}