package io.github.Huduong123.cart_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.Huduong123.cart_service.dto.client.product.ProductDetailDTO;

@FeignClient(name = "product-service", path = "/api/v1/internal")
public interface ProductServiceClient {

    @GetMapping("/product-details")
    ProductDetailDTO getProductDetails(
            @RequestParam("variantId") Long variantId,
            @RequestParam("sizeId") Long sizeId
    );
}