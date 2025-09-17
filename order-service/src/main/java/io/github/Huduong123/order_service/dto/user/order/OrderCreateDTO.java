package io.github.Huduong123.order_service.dto.user.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class OrderCreateDTO {

    @NotNull(message = "User Address ID cannot be null")
    private Long userAddressId; // ID địa chỉ người dùng đã chọn (lấy từ User Service)

    @NotNull(message = "Payment Method ID cannot be null")
    private Long paymentMethodId; // ID của phương thức thanh toán

    private String note; // Ghi chú của khách hàng
}