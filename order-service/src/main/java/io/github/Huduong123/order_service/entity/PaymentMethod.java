package io.github.Huduong123.order_service.entity;

import io.github.Huduong123.order_service.entity.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentMethodType type;
}