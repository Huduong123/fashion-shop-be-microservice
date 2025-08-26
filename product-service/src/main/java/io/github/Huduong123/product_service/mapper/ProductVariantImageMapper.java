package io.github.Huduong123.product_service.mapper;

import org.springframework.stereotype.Component;

import io.github.Huduong123.product_service.dto.admin.product.ProductVariantImageDTO;
import io.github.Huduong123.product_service.entity.ProductVariantImage;

@Component
public class ProductVariantImageMapper {

    public ProductVariantImageDTO convertToDTO(ProductVariantImage image) {
        if (image == null) {
            return null;
        }

        return ProductVariantImageDTO.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .altText(image.getAltText())
                .isPrimary(image.isPrimary())
                .displayOrder(image.getDisplayOrder())
                .build();
    }

    public ProductVariantImage convertToEntity(ProductVariantImageDTO dto) {
        if (dto == null) {
            return null;
        }

        return ProductVariantImage.builder()
                .imageUrl(dto.getImageUrl())
                .altText(dto.getAltText())
                .isPrimary(dto.isPrimary())
                .displayOrder(dto.getDisplayOrder())
                .build();
    }

    public void updateFromDTO(ProductVariantImageDTO dto, ProductVariantImage existingImage) {
        if (dto == null || existingImage == null) {
            return;
        }

        if (dto.getImageUrl() != null) {
            existingImage.setImageUrl(dto.getImageUrl());
        }
        if (dto.getAltText() != null) {
            existingImage.setAltText(dto.getAltText());
        }
        existingImage.setPrimary(dto.isPrimary());
        existingImage.setDisplayOrder(dto.getDisplayOrder());
    }
} 