package io.github.Huduong123.cart_service.service;

import io.github.Huduong123.cart_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartItemAddDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartItemUpdateDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartResponseDTO;

public interface CartService {
    CartResponseDTO getCartByUserId(Long userId);

    CartResponseDTO addItemToCart(Long userId, CartItemAddDTO addDTO);

    CartResponseDTO updateItemQuantity(Long userId, Long cartItemId, CartItemUpdateDTO updateDTO);

    ResponseMessageDTO removeItemFromCart(Long userId, Long cartItemId);

    ResponseMessageDTO clearCart(Long userId);
}
