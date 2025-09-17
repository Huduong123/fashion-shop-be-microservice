package io.github.Huduong123.order_service.dto.user.payment_method;

import io.github.Huduong123.order_service.entity.enums.PaymentMethodType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String imageUrl;
    private PaymentMethodType type;
}