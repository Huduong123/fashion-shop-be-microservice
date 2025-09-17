package io.github.Huduong123.cart_service.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.cart_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartItemAddDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartItemUpdateDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartResponseDTO;
import io.github.Huduong123.cart_service.service.CartService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private Long getUserIdFromPrincipal(Principal principal) {
        return Long.parseLong(principal.getName());
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getMyCart(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItemToMyCart(Principal principal, @Valid @RequestBody CartItemAddDTO addDTO) {
        Long userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(cartService.addItemToCart(userId, addDTO));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDTO> updateItemInMyCart(
            Principal principal,
            @PathVariable Long itemId,
            @Valid @RequestBody CartItemUpdateDTO updateDTO) {
        Long userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, itemId, updateDTO));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ResponseMessageDTO> removeItemFromMyCart(Principal principal, @PathVariable Long itemId) {
        Long userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, itemId));
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessageDTO> clearMyCart(Principal principal) {
        Long userId = getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

}
