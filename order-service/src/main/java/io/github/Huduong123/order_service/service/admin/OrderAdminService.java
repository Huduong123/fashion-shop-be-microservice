package io.github.Huduong123.order_service.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.Huduong123.order_service.dto.admin.order.OrderAdminResponseDTO;
import io.github.Huduong123.order_service.entity.enums.OrderStatus;
import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;
import java.time.LocalDate;

public interface OrderAdminService {
    Page<OrderAdminResponseDTO> getAllOrders(Pageable pageable, Long userId, OrderStatus status, LocalDate startDate, LocalDate endDate);

    OrderAdminResponseDTO getOrderById(Long orderId);

    ResponseMessageDTO updateOrderStatus(Long orderId, OrderStatus newStatus);

    ResponseMessageDTO deleteOrderAndRestoreStock(Long orderId);
}
