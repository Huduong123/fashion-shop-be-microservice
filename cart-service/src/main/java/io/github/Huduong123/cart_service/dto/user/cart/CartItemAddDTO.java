package io.github.Huduong123.cart_service.dto.user.cart;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dùng để thêm một sản phẩm mới vào giỏ hàng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemAddDTO {

    @NotNull(message = "Product Variant ID cannot be null")
    private Long productVariantId;

    @NotNull(message = "Size ID cannot be null")
    private Long sizeId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}