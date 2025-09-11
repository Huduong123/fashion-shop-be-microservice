package io.github.Huduong123.cart_service.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.Huduong123.cart_service.entity.CartItem;
import jakarta.transaction.Transactional;

@Repository
public interface  CartItemRepository extends JpaRepository<CartItem, Long>{
      /**
     * Tìm một mục trong giỏ hàng dựa trên userId, productVariantId và sizeId.
     * Đây là phương thức quan trọng nhất để kiểm tra xem một sản phẩm cụ thể (với màu và size)
     * đã tồn tại trong giỏ hàng của người dùng hay chưa, dùng cho logic thêm mới hoặc cập nhật số lượng.
     *
     * @param userId ID của người dùng.
     * @param productVariantId ID của biến thể sản phẩm.
     * @param sizeId ID của kích cỡ.
     * @return Optional chứa CartItem nếu tìm thấy.
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.userId = :userId " +
           "AND ci.productVariantId = :productVariantId AND ci.sizeId = :sizeId")
    Optional<CartItem> findByCartUserIdAndProductVariantIdAndSizeId(
            @Param("userId") Long userId,
            @Param("productVariantId") Long productVariantId,
            @Param("sizeId") Long sizeId
    );

    /**
     * Tải tất cả các mục trong giỏ hàng của một người dùng.
     * Trong kiến trúc microservice, chúng ta không cần JOIN FETCH sang các bảng product nữa
     * vì thông tin cần thiết để hiển thị (tên, giá, ảnh) đã được phi chuẩn hóa và lưu
     * ngay trong bảng cart_items.
     *
     * @param userId ID của người dùng.
     * @return Danh sách các CartItem trong giỏ hàng của người dùng.
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.userId = :userId ORDER BY ci.updatedAt DESC")
    List<CartItem> findAllByCartUserId(@Param("userId") Long userId);

    /**
     * Tìm một mục trong giỏ hàng bằng ID của nó và ID của người dùng.
     * Đây là một phương thức bảo mật quan trọng, đảm bảo rằng người dùng chỉ có thể
     * truy cập/thay đổi/xóa các mục trong giỏ hàng của chính họ.
     *
     * @param id ID của CartItem.
     * @param userId ID của người dùng.
     * @return Optional chứa CartItem nếu tìm thấy và khớp với người dùng.
     */
    @Query("SELECT ci FROM CartItem ci WHERE ci.id = :id AND ci.cart.userId = :userId")
    Optional<CartItem> findByIdAndCartUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * Xóa tất cả các mục trong giỏ hàng của một người dùng cụ thể.
     * Thường được gọi sau khi người dùng đã hoàn tất việc đặt hàng.
     * Sử dụng @Modifying và @Transactional vì đây là một câu lệnh DML.
     *
     * @param userId ID của người dùng.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id IN (SELECT c.id FROM Cart c WHERE c.userId = :userId)")
    void deleteAllByCartUserId(@Param("userId") Long userId);
}
