package io.github.Huduong123.order_service.dto.admin.payment_method;

import java.time.LocalDateTime;

import io.github.Huduong123.order_service.entity.enums.PaymentMethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodAdminDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String imageUrl;
    private PaymentMethodType type;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
