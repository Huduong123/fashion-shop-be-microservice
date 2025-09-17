package io.github.Huduong123.order_service.dto.user.order;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderShippingDetailResponseDTO {
    private String recipientName;
    private String phoneNumber;
    private String addressDetail;
    private BigDecimal shippingFee;
}