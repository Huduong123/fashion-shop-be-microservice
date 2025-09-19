package io.github.Huduong123.order_service.repository;

import io.github.Huduong123.order_service.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    /**
     * Tìm tất cả các phương thức thanh toán đang được kích hoạt (enabled).
     * Hữu ích để hiển thị cho người dùng lựa chọn khi checkout.
     */
    List<PaymentMethod> findByIsEnabledTrue();

    /**
     * Tìm một phương thức thanh toán bằng mã định danh duy nhất của nó.
     */
    Optional<PaymentMethod> findByCode(String code);
    Optional<PaymentMethod> findByName(String name);
}