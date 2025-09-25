package io.github.Huduong123.order_service.repository;

import io.github.Huduong123.order_service.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

       /**
        * Lấy danh sách tóm tắt đơn hàng của một người dùng, có phân trang.
        * Không cần JOIN FETCH phức tạp vì thông tin cần thiết đã có trong bảng Order.
        */
       Page<Order> findByUserId(Long userId, Pageable pageable);

       /**
        * Lấy chi tiết một đơn hàng, đảm bảo nó thuộc về đúng người dùng.
        * Sử dụng JOIN FETCH để tải sẵn các thông tin liên quan trong cùng service
        * (orderItems, shippingDetail) để tránh N+1 query.
        */
       @Query("SELECT o FROM Order o " +
                     "LEFT JOIN FETCH o.orderItems oi " +
                     "LEFT JOIN FETCH o.shippingDetail sd " +
                     "WHERE o.id = :orderId AND o.userId = :userId")
       Optional<Order> findByIdAndUserIdWithDetails(@Param("orderId") Long orderId, @Param("userId") Long userId);

       /**
        * Lấy chi tiết một đơn hàng bằng ID (thường dùng cho admin).
        * Tải sẵn các thông tin liên quan để xử lý.
        */
       @Query("SELECT o FROM Order o " +
                     "LEFT JOIN FETCH o.orderItems oi " +
                     "LEFT JOIN FETCH o.shippingDetail sd " +
                     "WHERE o.id = :orderId")
       Optional<Order> findByIdWithDetails(@Param("orderId") Long orderId);

       /**
        * Tìm một đơn hàng bằng mã đơn hàng (orderCode).
        */
       Optional<Order> findByOrderCode(String orderCode);

       /**
        * Kiểm tra xem có đơn hàng nào đang sử dụng một mã phương thức thanh toán cụ
        * thể không.
        * Hữu ích khi admin muốn xóa một phương thức thanh toán.
        *
        * @param paymentMethodCode Mã của phương thức thanh toán.
        * @return true nếu có ít nhất một đơn hàng sử dụng.
        */
       boolean existsByPaymentMethodCode(String paymentMethodCode);

       /**
        * Kiểm tra xem có đơn hàng nào sử dụng mã đơn hàng cụ thể không.
        * Dùng để đảm bảo mã đơn hàng là duy nhất.
        *
        * @param orderCode Mã đơn hàng cần kiểm tra.
        * @return true nếu mã đã tồn tại.
        */
       boolean existsByOrderCode(String orderCode);
}