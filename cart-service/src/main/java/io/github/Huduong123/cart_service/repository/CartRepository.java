package io.github.Huduong123.cart_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.Huduong123.cart_service.entity.Cart;

@Repository
public interface  CartRepository extends JpaRepository<Cart, Long>{
        /**
     * Tìm kiếm một giỏ hàng (Cart) dựa trên ID của người dùng (userId).
     * Đây là phương thức truy vấn cốt lõi, được sử dụng hầu hết trong các nghiệp vụ
     * để lấy giỏ hàng của người dùng hiện tại.
     *
     * @param userId ID của người dùng từ User Service.
     * @return một Optional chứa Cart nếu tìm thấy, ngược lại là Optional rỗng.
     */
    Optional<Cart> findByUserId(Long userId);

    /**
     * Xóa một giỏ hàng dựa trên ID của người dùng.
     * Hữu ích khi người dùng xóa tài khoản hoặc khi cần dọn dẹp dữ liệu.
     *
     * @param userId ID của người dùng từ User Service.
     */
    void deleteByUserId(Long userId);

    /**
     * Kiểm tra xem một giỏ hàng có tồn tại cho một người dùng cụ thể hay không.
     * Hiệu quả hơn findByUserId khi bạn chỉ cần câu trả lời có/không.
     *
     * @param userId ID của người dùng từ User Service.
     * @return true nếu giỏ hàng tồn tại, ngược lại là false.
     */
    boolean existsByUserId(Long userId);
}
