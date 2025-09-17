package io.github.Huduong123.cart_service.service;


import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Huduong123.cart_service.client.ProductServiceClient;
import io.github.Huduong123.cart_service.dto.client.product.ProductDetailDTO;
import io.github.Huduong123.cart_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartItemAddDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartItemUpdateDTO;
import io.github.Huduong123.cart_service.dto.user.cart.CartResponseDTO;
import io.github.Huduong123.cart_service.entity.Cart;
import io.github.Huduong123.cart_service.entity.CartItem;
import io.github.Huduong123.cart_service.exeption.NotFoundException;
import io.github.Huduong123.cart_service.mapper.CartMapper;
import io.github.Huduong123.cart_service.repository.CartItemRepository;
import io.github.Huduong123.cart_service.repository.CartRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final ProductServiceClient productServiceClient;

    private static final int PURCHASE_LIMIT = 10;

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return cartMapper.toCartResponseDTO(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addItemToCart(Long userId, CartItemAddDTO addDTO) {
        // 1. Gọi API trực tiếp sang Product Service để lấy thông tin và kiểm tra tồn kho
        ProductDetailDTO productDetails = productServiceClient.getProductDetails(
                addDTO.getProductVariantId(), addDTO.getSizeId());

        // 2. Lấy hoặc tạo giỏ hàng
        Cart cart = getOrCreateCart(userId);

        // 3. Tìm xem sản phẩm đã có trong giỏ chưa
        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProductVariantId().equals(addDTO.getProductVariantId())
                        && item.getSizeId().equals(addDTO.getSizeId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // 4a. Cập nhật số lượng nếu đã có
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + addDTO.getQuantity();
            
            checkPurchaseLimit(newQuantity);
            checkStockAvailability(productDetails, newQuantity);
            existingItem.setQuantity(newQuantity);
        } else {
            // 4b. Tạo mới nếu chưa có
            checkPurchaseLimit(addDTO.getQuantity());
            checkStockAvailability(productDetails, addDTO.getQuantity());

            CartItem newItem = CartItem.builder()
                    .productId(productDetails.getProductId())
                    .productVariantId(productDetails.getProductVariantId())
                    .sizeId(productDetails.getSizeId())
                    .productName(productDetails.getProductName())
                    .colorName(productDetails.getColorName())
                    .sizeName(productDetails.getSizeName())
                    .imageUrl(productDetails.getImageUrl())
                    .price(productDetails.getPrice())
                    .quantity(addDTO.getQuantity())
                    .build();
            cart.addCartItem(newItem);
        }

        // 5. Lưu và trả về trạng thái mới của toàn bộ giỏ hàng
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDTO(savedCart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateItemQuantity(Long userId, Long cartItemId, CartItemUpdateDTO updateDTO) {
        CartItem cartItem = getCartItemForUser(cartItemId, userId);
        
        // Gọi API sang Product Service để kiểm tra tồn kho với số lượng mới
        ProductDetailDTO productDetails = productServiceClient.getProductDetails(
                cartItem.getProductVariantId(), cartItem.getSizeId());
        
        checkPurchaseLimit(updateDTO.getQuantity());
        checkStockAvailability(productDetails, updateDTO.getQuantity());

        cartItem.setQuantity(updateDTO.getQuantity());
        CartItem updatedItem = cartItemRepository.save(cartItem);

        return cartMapper.toCartResponseDTO(updatedItem.getCart());
    }

    @Override
    @Transactional
    public ResponseMessageDTO removeItemFromCart(Long userId, Long cartItemId) {
        CartItem cartItem = getCartItemForUser(cartItemId, userId);
        cartItemRepository.delete(cartItem);
        return new ResponseMessageDTO(HttpStatus.OK, "Removed item successfully.");
    }

    @Override
    @Transactional
    public ResponseMessageDTO clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        if (!cart.getCartItems().isEmpty()) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }
        return new ResponseMessageDTO(HttpStatus.OK, "Cart cleared successfully.");
    }

    // --- Private Helper Methods ---

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).build()));
    }

    private CartItem getCartItemForUser(Long cartItemId, Long userId) {
        return cartItemRepository.findByIdAndCartUserId(cartItemId, userId)
                .orElseThrow(() -> new NotFoundException("Cart item with ID " + cartItemId + " not found in your cart."));
    }

    private void checkStockAvailability(ProductDetailDTO product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new IllegalArgumentException("Not enough stock. Only " + product.getStock() + " items available.");
        }
    }

    private void checkPurchaseLimit(int totalQuantity) {
        if (totalQuantity > PURCHASE_LIMIT) {
            throw new IllegalArgumentException("Purchase limit exceeded. You can only buy a maximum of " + PURCHASE_LIMIT + " items.");
        }
    }
}