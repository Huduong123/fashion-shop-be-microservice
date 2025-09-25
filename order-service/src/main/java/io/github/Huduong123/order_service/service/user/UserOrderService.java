package io.github.Huduong123.order_service.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderCreateDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderResponseDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderSummaryDTO;

public interface UserOrderService {

    Page<OrderSummaryDTO> getOrderHistory(Long userId, Pageable pageable);

    OrderResponseDTO getOrderDetails(Long userId, Long orderId);

    OrderResponseDTO createOrderFromCart(Long userId, OrderCreateDTO createDTO, String authToken);

    ResponseMessageDTO cancelOrder(Long userId, Long orderId);

    ResponseMessageDTO confirmOrderDelivered(Long userId, Long orderId);
}