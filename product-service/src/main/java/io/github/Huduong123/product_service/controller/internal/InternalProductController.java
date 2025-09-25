package io.github.Huduong123.product_service.controller.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.product_service.dto.internal.InternalProductDetailDTO;
import io.github.Huduong123.product_service.dto.internal.StockAdjustmentRequest;
import io.github.Huduong123.product_service.service.user.InternalProductService;

@RestController
@RequestMapping("/api/v1/internal") // Sử dụng prefix /internal

public class InternalProductController {

    private final InternalProductService internalProductService;

    public InternalProductController(InternalProductService internalProductService) {
        this.internalProductService = internalProductService;
    }

    @GetMapping("/product-details")
    public ResponseEntity<InternalProductDetailDTO> getProductDetails(
            @RequestParam("variantId") Long variantId,
            @RequestParam("sizeId") Long sizeId) {

        InternalProductDetailDTO details = internalProductService.getProductDetailsForCart(variantId, sizeId);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/products/adjust-stock")
    public ResponseEntity<Void> adjustStock(@RequestBody StockAdjustmentRequest request) {
        internalProductService.adjustStock(request);
        return ResponseEntity.ok().build();
    }
}