package io.github.Huduong123.product_service.service.user;


import io.github.Huduong123.product_service.dto.internal.InternalProductDetailDTO;
import io.github.Huduong123.product_service.entity.ProductVariant;
import io.github.Huduong123.product_service.entity.ProductVariantSize;
import io.github.Huduong123.product_service.exception.NotFoundException;
import io.github.Huduong123.product_service.repository.ProductVariantRepository;
import io.github.Huduong123.product_service.repository.ProductVariantSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InternalProductServiceImpl implements InternalProductService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductVariantSizeRepository productVariantSizeRepository;

    @Override
    @Transactional(readOnly = true)
    public InternalProductDetailDTO getProductDetailsForCart(Long variantId, Long sizeId) {
        // 1. Tìm biến thể sản phẩm
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Product variant not found with id: " + variantId));

        // 2. Tìm chi tiết size của biến thể đó
        ProductVariantSize pvs = productVariantSizeRepository.findByProductVariantIdAndSizeId(variantId, sizeId)
                .orElseThrow(() -> new NotFoundException("Size not available for this product variant. VariantId: " + variantId + ", SizeId: " + sizeId));

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
}
