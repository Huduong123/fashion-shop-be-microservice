package io.github.Huduong123.order_service.repository;

import io.github.Huduong123.order_service.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    /**
     * Tìm một giao dịch bằng mã giao dịch do hệ thống của mình tạo ra.
     */
    Optional<PaymentTransaction> findByTransactionCode(String transactionCode);

    /**
     * Tìm một giao dịch bằng mã giao dịch từ cổng thanh toán (để xử lý callback/IPN).
     */
    Optional<PaymentTransaction> findByGatewayTransactionId(String gatewayTransactionId);

    /**
     * Tìm tất cả các giao dịch của một đơn hàng.
     */
    List<PaymentTransaction> findByOrderId(Long orderId);
}