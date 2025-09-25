package io.github.Huduong123.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import io.github.Huduong123.order_service.dto.client.UserAddressDTO;

// Gọi đến user-service thông qua API Gateway
@FeignClient(name = "gateway-service", contextId = "userServiceClient", path = "/api/v1/user")
public interface UserServiceClient {

    @GetMapping("/address/{addressId}")
    UserAddressDTO getUserAddress(@PathVariable("addressId") Long addressId,
            @RequestHeader("Authorization") String token);
}