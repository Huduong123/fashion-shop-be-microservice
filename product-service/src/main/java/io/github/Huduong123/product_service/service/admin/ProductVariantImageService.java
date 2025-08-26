package io.github.Huduong123.product_service.service.admin;

import java.util.List;

import io.github.Huduong123.product_service.dto.admin.product.ProductVariantImageDTO;
import io.github.Huduong123.product_service.dto.common.ResponseMessageDTO;

public interface ProductVariantImageService {

    List<ProductVariantImageDTO> getImagesByVariantId(Long variantId);

    ProductVariantImageDTO addImageToVariant(Long variantId, ProductVariantImageDTO imageDTO);

    ProductVariantImageDTO updateImage(Long imageId, ProductVariantImageDTO imageDTO);

    ResponseMessageDTO deleteImage(Long imageId);

    List<ProductVariantImageDTO> setPrimaryImage(Long imageId);

    ProductVariantImageDTO getPrimaryImage(Long variantId);

    void reorderImages(Long variantId, List<Long> imageIds);

    void deleteAllImagesByVariantId(Long variantId);
} 