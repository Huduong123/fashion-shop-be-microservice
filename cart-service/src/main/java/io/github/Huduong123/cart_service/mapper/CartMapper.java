package io.github.Huduong123.cart_service.mapper;


import io.github.Huduong123.cart_service.entity.Cart;
import io.github.Huduong123.cart_service.entity.CartItem;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.github.Huduong123.cart_service.dto.user.cart.CartItemResponseDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartResponseDTO;

/**
 * Mapper chịu trách nhiệm chuyển đổi giữa các đối tượng Entity (Cart, CartItem)
 * và các đối tượng DTO (CartResponseDTO, CartItemResponseDTO).
 */
@Component
public class CartMapper {

    /**
     * Chuyển đổi từ entity Cart sang CartResponseDTO.
     * Đây là phương thức chính để tạo response cho API lấy thông tin toàn bộ giỏ hàng.
     * Nó bao gồm cả việc ánh xạ và tính toán các giá trị tổng hợp.
     *
     * @param cart Entity Cart lấy từ database.
     * @return CartResponseDTO chứa thông tin tổng hợp.
     */
    public CartResponseDTO toCartResponseDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        List<CartItem> items = cart.getCartItems() != null ? cart.getCartItems() : Collections.emptyList();
 
        List<CartItemResponseDTO> itemDTOs = items.stream()
                .map(this::toCartItemResponseDTO)
                .collect(Collectors.toList());

        BigDecimal totalPrice = itemDTOs.stream()
                .map(CartItemResponseDTO::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        int totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        return CartResponseDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .cartItems(itemDTOs)
                .totalItems(totalItems)
                .distinctItems(items.size())
                .totalPrice(totalPrice)
                .build();
    }

    /**
     * Chuyển đổi từ entity CartItem sang CartItemResponseDTO.
     * Phương thức này ánh xạ trực tiếp các trường đã được phi chuẩn hóa từ entity
     * và tính toán thành tiền (subTotal).
     *
     * @param cartItem Entity CartItem lấy từ database.
     * @return CartItemResponseDTO.
     */
    public CartItemResponseDTO toCartItemResponseDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        BigDecimal price = cartItem.getPrice() != null ? cartItem.getPrice() : BigDecimal.ZERO;
        Integer quantity = cartItem.getQuantity() != null ? cartItem.getQuantity() : 0;
        BigDecimal subTotal = price.multiply(new BigDecimal(quantity));

        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProductId())
                .productVariantId(cartItem.getProductVariantId())
                .sizeId(cartItem.getSizeId())
                .productName(cartItem.getProductName())
                .colorName(cartItem.getColorName())
                .sizeName(cartItem.getSizeName())
                .imageUrl(cartItem.getImageUrl())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .subTotal(subTotal)
                .build();
    }
}