package io.github.Huduong123.product_service.service.user;

import io.github.Huduong123.product_service.dto.internal.InternalProductDetailDTO;
import io.github.Huduong123.product_service.dto.internal.StockAdjustmentRequest;

public interface InternalProductService {
    InternalProductDetailDTO getProductDetailsForCart(Long variantId, Long sizeId);

    void adjustStock(StockAdjustmentRequest request);
}