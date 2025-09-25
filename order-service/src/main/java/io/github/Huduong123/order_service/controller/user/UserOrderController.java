package io.github.Huduong123.order_service.controller.user;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderCreateDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderResponseDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderSummaryDTO;
import io.github.Huduong123.order_service.service.user.UserOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller xử lý các API liên quan đến đơn hàng cho người dùng thông thường.
 * Bao gồm: xem lịch sử đơn hàng, tạo đơn hàng, hủy đơn hàng, xác nhận nhận
 * hàng.
 */
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Slf4j
public class UserOrderController {

    private final UserOrderService userOrderService;

    /**
     * Utility method để lấy userId từ Principal (được set bởi authentication
     * filter)
     */
    private Long getUserIdFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new IllegalArgumentException("User authentication required");
        }
        try {
            return Long.valueOf(principal.getName());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID format");
        }
    }

    /**
     * Lấy lịch sử đơn hàng của người dùng hiện tại với phân trang
     * GET /api/v1/order/history
     */
    @GetMapping("/history")
    public ResponseEntity<Page<OrderSummaryDTO>> getOrderHistory(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        try {
            Long userId = getUserIdFromPrincipal(principal);
            log.info("Getting order history for user ID: {}, page: {}, size: {}", userId, page, size);

            // Parse sort parameter
            Sort.Direction direction = Sort.Direction.DESC;
            String sortField = "createdAt";

            if (sort.contains(",")) {
                String[] sortParts = sort.split(",");
                sortField = sortParts[0];
                direction = "asc".equalsIgnoreCase(sortParts[1]) ? Sort.Direction.ASC : Sort.Direction.DESC;
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
            Page<OrderSummaryDTO> orderHistory = userOrderService.getOrderHistory(userId, pageable);

            log.info("Retrieved {} orders for user ID: {}", orderHistory.getTotalElements(), userId);
            return ResponseEntity.ok(orderHistory);

        } catch (Exception e) {
            log.error("Error getting order history", e);
            throw e; // Let GlobalExceptionHandler handle this
        }
    }

    /**
     * Lấy chi tiết một đơn hàng cụ thể
     * GET /api/v1/order/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderDetails(
            Principal principal,
            @PathVariable Long orderId) {

        try {
            Long userId = getUserIdFromPrincipal(principal);
            log.info("Getting order details for order ID: {} and user ID: {}", orderId, userId);

            OrderResponseDTO orderDetails = userOrderService.getOrderDetails(userId, orderId);

            log.info("Retrieved order details for order code: {}", orderDetails.getOrderCode());
            return ResponseEntity.ok(orderDetails);

        } catch (Exception e) {
            log.error("Error getting order details for order ID: {}", orderId, e);
            throw e; // Let GlobalExceptionHandler handle this
        }
    }

    /**
     * Tạo đơn hàng mới từ giỏ hàng
     * POST /api/v1/order
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrderFromCart(
            Principal principal,
            @Valid @RequestBody OrderCreateDTO createDTO,
            @RequestHeader("Authorization") String authToken) {

        try {
            Long userId = getUserIdFromPrincipal(principal);
            log.info("Creating order from cart for user ID: {} with address ID: {} and payment method ID: {}",
                    userId, createDTO.getUserAddressId(), createDTO.getPaymentMethodId());

            OrderResponseDTO createdOrder = userOrderService.createOrderFromCart(userId, createDTO, authToken);

            log.info("Order created successfully with code: {} for user ID: {}",
                    createdOrder.getOrderCode(), userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);

        } catch (Exception e) {
            log.error("Error creating order from cart for user ID: {}",
                    principal != null ? principal.getName() : "unknown", e);
            throw e; // Let GlobalExceptionHandler handle this
        }
    }

    /**
     * Hủy đơn hàng
     * PUT /api/v1/order/{orderId}/cancel
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ResponseMessageDTO> cancelOrder(
            Principal principal,
            @PathVariable Long orderId) {

        try {
            Long userId = getUserIdFromPrincipal(principal);
            log.info("Cancelling order ID: {} for user ID: {}", orderId, userId);

            ResponseMessageDTO response = userOrderService.cancelOrder(userId, orderId);

            log.info("Order ID: {} cancelled successfully for user ID: {}", orderId, userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error cancelling order ID: {} for user ID: {}", orderId,
                    principal != null ? principal.getName() : "unknown", e);
            throw e; // Let GlobalExceptionHandler handle this
        }
    }

    /**
     * Xác nhận đã nhận được hàng (hoàn thành đơn hàng)
     * PUT /api/v1/order/{orderId}/confirm-delivery
     */
    @PutMapping("/{orderId}/confirm-delivery")
    public ResponseEntity<ResponseMessageDTO> confirmOrderDelivery(
            Principal principal,
            @PathVariable Long orderId) {

        try {
            Long userId = getUserIdFromPrincipal(principal);
            log.info("Confirming delivery for order ID: {} and user ID: {}", orderId, userId);

            ResponseMessageDTO response = userOrderService.confirmOrderDelivered(userId, orderId);

            log.info("Delivery confirmed successfully for order ID: {} and user ID: {}", orderId, userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error confirming delivery for order ID: {} and user ID: {}", orderId,
                    principal != null ? principal.getName() : "unknown", e);
            throw e; // Let GlobalExceptionHandler handle this
        }
    }
}
