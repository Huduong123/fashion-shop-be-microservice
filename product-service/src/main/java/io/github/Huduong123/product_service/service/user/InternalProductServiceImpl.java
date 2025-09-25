package io.github.Huduong123.product_service.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Huduong123.product_service.dto.internal.InternalProductDetailDTO;
import io.github.Huduong123.product_service.dto.internal.StockAdjustmentRequest;
import io.github.Huduong123.product_service.entity.ProductVariant;
import io.github.Huduong123.product_service.entity.ProductVariantSize;
import io.github.Huduong123.product_service.exception.NotFoundException;
import io.github.Huduong123.product_service.repository.ProductVariantRepository;
import io.github.Huduong123.product_service.repository.ProductVariantSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalProductServiceImpl implements InternalProductService {

        private final ProductVariantRepository productVariantRepository;
        private final ProductVariantSizeRepository productVariantSizeRepository;

        @Override
        @Transactional(readOnly = true)
        public InternalProductDetailDTO getProductDetailsForCart(Long variantId, Long sizeId) {
                // 1. Tìm biến thể sản phẩm
                ProductVariant variant = productVariantRepository.findById(variantId)
                                .orElseThrow(() -> new NotFoundException(
                                                "Product variant not found with id: " + variantId));

                // 2. Tìm chi tiết size của biến thể đó
                ProductVariantSize pvs = productVariantSizeRepository.findByProductVariantIdAndSizeId(variantId, sizeId)
                                .orElseThrow(() -> new NotFoundException(
                                                "Size not available for this product variant. VariantId: " + variantId
                                                                + ", SizeId: " + sizeId));

                // 3. Xây dựng và trả về DTO
                return InternalProductDetailDTO.builder()
                                .productId(variant.getProduct().getId())
                                .productVariantId(variant.getId())
                                .sizeId(pvs.getSize().getId())
                                .productName(variant.getProduct().getName())
                                .colorName(variant.getColor().getName())
                                .sizeName(pvs.getSize().getName())
                                .imageUrl(variant.getPrimaryImageUrl()) // Lấy ảnh chính
                                .price(pvs.getPrice())
                                .stock(pvs.getQuantity())
                                .build();
        }

        @Override
        @Transactional
        public void adjustStock(StockAdjustmentRequest request) {
                log.info("Processing stock adjustment request for {} items", request.getItems().size());

                for (StockAdjustmentRequest.StockItem item : request.getItems()) {
                        // Tìm ProductVariantSize để cập nhật tồn kho
                        ProductVariantSize pvs = productVariantSizeRepository
                                        .findByProductVariantIdAndSizeId(item.getProductVariantId(), item.getSizeId())
                                        .orElseThrow(() -> new NotFoundException(
                                                        String.format("Product variant size not found. VariantId: %d, SizeId: %d",
                                                                        item.getProductVariantId(), item.getSizeId())));

                        // Kiểm tra tồn kho trước khi trừ
                        int currentStock = pvs.getQuantity();
                        int newStock = currentStock - item.getQuantity();

                        if (newStock < 0) {
                                log.error("Insufficient stock for ProductVariant {} Size {}. Current: {}, Requested: {}",
                                                item.getProductVariantId(), item.getSizeId(), currentStock,
                                                item.getQuantity());
                                throw new IllegalStateException(
                                                String.format("Insufficient stock. Available: %d, Requested: %d",
                                                                currentStock, item.getQuantity()));
                        }

                        // Cập nhật tồn kho
                        pvs.setQuantity(newStock);
                        productVariantSizeRepository.save(pvs);

                        log.debug("Stock adjusted for ProductVariant {} Size {}: {} -> {}",
                                        item.getProductVariantId(), item.getSizeId(), currentStock, newStock);
                }

                log.info("Stock adjustment completed successfully for {} items", request.getItems().size());
        }
}
