package io.github.Huduong123.order_service.repository;

import io.github.Huduong123.order_service.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Hiện tại có thể chưa cần phương thức nào đặc biệt ở đây.
    // Spring Data JPA đã cung cấp đủ các phương thức CRUD cơ bản.
    // Có thể thêm các phương thức truy vấn thống kê sau này nếu cần.
    // Ví dụ: List<OrderItem> findByProductId(Long productId);
}