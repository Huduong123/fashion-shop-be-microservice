package io.github.Huduong123.order_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.Huduong123.order_service.entity.enums.OrderStatus;
import io.github.Huduong123.order_service.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Column(name = "order_code", nullable = false, unique = true, length = 20)
    private String orderCode;

    /**
     * ID của người dùng từ User Service. Đây là một ID, không phải là một foreign key.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    /**
     * Dữ liệu snapshot của phương thức thanh toán tại thời điểm đặt hàng.
     */
    @Column(name = "payment_method_code", nullable = false, length = 50)
    private String paymentMethodCode;

    @Column(name = "payment_method_name", nullable = false, length = 100)
    private String paymentMethodName;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private OrderShippingDetail shippingDetail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<PaymentTransaction> paymentTransactions = new ArrayList<>();

    // Helper methods
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void setShippingDetail(OrderShippingDetail detail) {
        this.shippingDetail = detail;
        detail.setOrder(this);
    }
}