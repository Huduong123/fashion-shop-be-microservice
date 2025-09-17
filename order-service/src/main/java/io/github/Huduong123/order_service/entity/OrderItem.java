package io.github.Huduong123.order_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    // --- Dữ liệu tham chiếu từ Product Service ---
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_variant_id", nullable = false)
    private Long productVariantId;

    @Column(name = "size_id", nullable = false)
    private Long sizeId;

    // --- Dữ liệu được "snapshot" lại để đảm bảo tính lịch sử ---
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "color_name", length = 50)
    private String colorName;

    @Column(name = "size_name", nullable = false, length = 20)
    private String sizeName;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}