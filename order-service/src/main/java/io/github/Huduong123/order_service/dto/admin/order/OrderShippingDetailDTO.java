package io.github.Huduong123.order_service.dto.admin.order;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class OrderShippingDetailDTO {
    private String recipientName;
    private String phoneNumber;
    private String addressDetail;
    private BigDecimal shippingFee;
}