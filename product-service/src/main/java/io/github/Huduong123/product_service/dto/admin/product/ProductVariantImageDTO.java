package io.github.Huduong123.product_service.dto.admin.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantImageDTO {
    
    private Long id;
    private String imageUrl;
    private String altText;
    private boolean isPrimary;
    private int displayOrder;
} 