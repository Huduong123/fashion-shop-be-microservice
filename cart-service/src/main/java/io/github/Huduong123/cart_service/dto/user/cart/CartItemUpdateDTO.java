package io.github.Huduong123.cart_service.dto.user.cart;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO dùng để cập nhật số lượng của một item trong giỏ hàng.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateDTO {

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}