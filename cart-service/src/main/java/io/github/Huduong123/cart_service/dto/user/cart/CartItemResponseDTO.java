package io.github.Huduong123.cart_service.dto.user.cart;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO trả về thông tin chi tiết của một item trong giỏ hàng.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponseDTO {
    // ID của chính cart item, hữu ích cho việc cập nhật/xóa
    private Long id;

    // IDs tham chiếu
    private Long productId;
    private Long productVariantId;
    private Long sizeId;

    // Dữ liệu phi chuẩn hóa để hiển thị
    private String productName;
    private String colorName;
    private String sizeName;
    private String imageUrl;
    private BigDecimal price; // Giá tại thời điểm thêm vào giỏ
    private int quantity;

    // Dữ liệu được tính toán
    private BigDecimal subTotal; // Thành tiền (price * quantity)
}