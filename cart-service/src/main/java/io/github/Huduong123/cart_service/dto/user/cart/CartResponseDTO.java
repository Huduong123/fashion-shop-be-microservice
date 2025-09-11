package io.github.Huduong123.cart_service.dto.user.cart;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO trả về thông tin toàn bộ giỏ hàng của người dùng.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {

    private Long id; // ID của giỏ hàng
    private Long userId;

    // Danh sách các sản phẩm trong giỏ
    private List<CartItemResponseDTO> cartItems;

    // Thông tin tổng hợp cho toàn bộ giỏ hàng
    private Integer totalItems; // Tổng số lượng tất cả sản phẩm
    private Integer distinctItems; // Số loại sản phẩm khác nhau
    private BigDecimal totalPrice; // Tổng tiền của cả giỏ hàng
}
