package io.github.Huduong123.order_service.service.user;

import io.github.Huduong123.order_service.client.*;
import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderCreateDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderResponseDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderSummaryDTO;
import io.github.Huduong123.order_service.entity.*;
import io.github.Huduong123.order_service.entity.enums.OrderStatus;
import io.github.Huduong123.order_service.entity.enums.PaymentStatus;
import io.github.Huduong123.order_service.events.dto.OrderCancelledEvent;
import io.github.Huduong123.order_service.events.dto.OrderCreatedEvent;
import io.github.Huduong123.order_service.events.producer.OrderEventProducer;
import io.github.Huduong123.order_service.exception.NotFoundException;
import io.github.Huduong123.order_service.mapper.OrderMapper;
import io.github.Huduong123.order_service.repository.OrderRepository;
import io.github.Huduong123.order_service.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import io.github.Huduong123.order_service.dto.client.CartDetailDTO;
import io.github.Huduong123.order_service.dto.client.StockAdjustmentRequest;
import io.github.Huduong123.order_service.dto.client.UserAddressDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOrderServiceImpl implements UserOrderService {

    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderMapper orderMapper;
    private final UserServiceClient userClient;
    private final CartServiceClient cartClient;
    private final ProductServiceClient productClient;
    private final OrderEventProducer orderEventProducer;

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryDTO> getOrderHistory(Long userId, Pageable pageable) {
        try {
            log.info("Fetching order history for user ID: {} with page: {}, size: {}",
                    userId, pageable.getPageNumber(), pageable.getPageSize());

            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }

            Page<Order> ordersPage = orderRepository.findByUserId(userId, pageable);
            Page<OrderSummaryDTO> result = ordersPage.map(orderMapper::toOrderSummaryDTO);

            log.info("Found {} orders for user ID: {}", result.getTotalElements(), userId);
            return result;

        } catch (Exception e) {
            log.error("Error fetching order history for user ID: {}", userId, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderDetails(Long userId, Long orderId) {
        try {
            log.info("Fetching order details for order ID: {} and user ID: {}", orderId, userId);

            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }

            Order order = orderRepository.findByIdAndUserIdWithDetails(orderId, userId)
                    .orElseThrow(() -> new NotFoundException(
                            "Order not found with id: " + orderId + " for user: " + userId));

            OrderResponseDTO result = orderMapper.toOrderResponseDTO(order);
            log.info("Successfully fetched order details for order code: {}", order.getOrderCode());

            return result;

        } catch (Exception e) {
            log.error("Error fetching order details for order ID: {} and user ID: {}", orderId, userId, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrderFromCart(Long userId, OrderCreateDTO createDTO, String authToken) {
        try {
            // === VALIDATION ===
            log.info("Creating order from cart for user ID: {}", userId);
            validateOrderCreateRequest(userId, createDTO, authToken);

            // === BƯỚC 1: ĐIỀU PHỐI - LẤY DỮ LIỆU TỪ CÁC SERVICE KHÁC ===
            log.info("Step 1: Orchestrating data fetching for user ID: {}", userId);

            UserAddressDTO address;
            CartDetailDTO cart;
            PaymentMethod paymentMethod;

            try {
                address = userClient.getUserAddress(createDTO.getUserAddressId(), authToken);
                log.debug("Retrieved address for user address ID: {}", createDTO.getUserAddressId());
            } catch (Exception e) {
                log.error("Failed to get user address with ID: {}", createDTO.getUserAddressId(), e);
                throw new NotFoundException("User address not found with id: " + createDTO.getUserAddressId());
            }

            try {
                cart = cartClient.getCartDetails(authToken);
                log.debug("Retrieved cart with {} items", cart.getCartItems() != null ? cart.getCartItems().size() : 0);
            } catch (Exception e) {
                log.error("Failed to get cart details", e);
                throw new IllegalStateException("Unable to retrieve cart details. Please try again.");
            }

            paymentMethod = paymentMethodRepository.findById(createDTO.getPaymentMethodId())
                    .orElseThrow(() -> new NotFoundException(
                            "Payment method not found with id: " + createDTO.getPaymentMethodId()));

            if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
                throw new IllegalStateException("Cannot create order from an empty cart.");
            }

            // Validate cart items
            validateCartItems(cart);

            // === BƯỚC 2: THỰC THI LỆNH ĐỒNG BỘ - TRỪ KHO ===
            log.info("Step 2: Requesting stock deduction from Product Service");
            List<StockAdjustmentRequest.StockItem> itemsToDeduct = cart.getCartItems().stream()
                    .map(item -> StockAdjustmentRequest.StockItem.builder()
                            .productVariantId(item.getProductVariantId())
                            .sizeId(item.getSizeId())
                            .quantity(item.getQuantity())
                            .build())
                    .collect(Collectors.toList());

            try {
                productClient.adjustStock(StockAdjustmentRequest.builder().items(itemsToDeduct).build());
                log.info("Stock deduction successful for {} items", itemsToDeduct.size());
            } catch (Exception e) {
                log.error("Stock deduction failed for user ID: {}", userId, e);
                throw new IllegalStateException(
                        "Unable to reserve stock for your order. Some items may be out of stock.");
            }

            // === BƯỚC 3: XỬ LÝ NGHIỆP VỤ NỘI BỘ - TẠO ORDER ENTITY ===
            log.info("Step 3: Building order entity");
            Order newOrder = buildOrder(userId, createDTO, address, cart, paymentMethod);

            // === BƯỚC 4: LƯU TRẠNG THÁI VÀO DB CỦA ORDER-SERVICE ===
            Order savedOrder = orderRepository.save(newOrder);
            log.info("Step 4: Order {} saved successfully for user ID: {}", savedOrder.getOrderCode(), userId);

            // === BƯỚC 5: PHÁT SỰ KIỆN BẤT ĐỒNG BỘ - THÔNG BÁO CHO CÁC SERVICE KHÁC ===
            try {
                OrderCreatedEvent event = OrderCreatedEvent.builder()
                        .userId(userId)
                        .orderId(savedOrder.getId())
                        .orderCode(savedOrder.getOrderCode())
                        .build();
                orderEventProducer.sendOrderCreatedEvent(event);
                log.info("Step 5: Published OrderCreatedEvent for order ID: {}", savedOrder.getId());
            } catch (Exception e) {
                log.warn("Failed to publish OrderCreatedEvent for order ID: {}, but order was created successfully",
                        savedOrder.getId(), e);
                // Don't fail the entire operation if event publishing fails
            }

            return orderMapper.toOrderResponseDTO(savedOrder);

        } catch (IllegalArgumentException | IllegalStateException | NotFoundException e) {
            log.error("Business logic error in createOrderFromCart for user ID: {}", userId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in createOrderFromCart for user ID: {}", userId, e);
            throw new RuntimeException("Failed to create order. Please try again.", e);
        }
    }

    @Override
    @Transactional
    public ResponseMessageDTO cancelOrder(Long userId, Long orderId) {
        try {
            log.info("Cancelling order ID: {} for user ID: {}", orderId, userId);

            // Validate inputs
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }

            Order order = findOrderForUser(orderId, userId);

            // Validate order status
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new IllegalStateException(
                        String.format(
                                "Cannot cancel order %s. Only orders in PENDING status can be cancelled. Current status: %s",
                                order.getOrderCode(), order.getStatus()));
            }

            // Check if order has items to restore stock
            if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
                log.warn("Order {} has no items to restore stock for", order.getOrderCode());
            }

            // Update order status
            order.setStatus(OrderStatus.CANCELLED);
            Order savedOrder = orderRepository.save(order);
            log.info("Order {} status updated to CANCELLED", savedOrder.getOrderCode());

            // Phát sự kiện để Product Service hoàn kho một cách bất đồng bộ
            try {
                List<OrderCancelledEvent.OrderItemPayload> itemsToRestore = order.getOrderItems().stream()
                        .map(item -> OrderCancelledEvent.OrderItemPayload.builder()
                                .productVariantId(item.getProductVariantId())
                                .sizeId(item.getSizeId())
                                .quantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList());

                OrderCancelledEvent event = OrderCancelledEvent.builder()
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .itemsToRestore(itemsToRestore)
                        .build();

                orderEventProducer.sendOrderCancelledEvent(event);
                log.info("Published OrderCancelledEvent for order ID: {}. Stock will be restored asynchronously.",
                        order.getId());
            } catch (Exception e) {
                log.error(
                        "Failed to publish OrderCancelledEvent for order ID: {}, but order was cancelled successfully",
                        order.getId(), e);
                // Don't fail the entire operation if event publishing fails
            }

            return new ResponseMessageDTO(HttpStatus.OK, "Order cancelled successfully.");

        } catch (IllegalArgumentException | IllegalStateException | NotFoundException e) {
            log.error("Business logic error in cancelOrder for order ID: {} and user ID: {}", orderId, userId, e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in cancelOrder for order ID: {} and user ID: {}", orderId, userId, e);
            throw new RuntimeException("Failed to cancel order. Please try again.", e);
        }
    }

    @Override
    @Transactional
    public ResponseMessageDTO confirmOrderDelivered(Long userId, Long orderId) {
        try {
            log.info("Confirming delivery for order ID: {} and user ID: {}", orderId, userId);

            // Validate inputs
            if (userId == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }
            if (orderId == null) {
                throw new IllegalArgumentException("Order ID cannot be null");
            }

            Order order = findOrderForUser(orderId, userId);

            // Validate order status
            if (order.getStatus() != OrderStatus.DELIVERED) {
                throw new IllegalStateException(
                        String.format(
                                "Cannot confirm delivery for order %s. Only DELIVERED orders can be marked as COMPLETED. Current status: %s",
                                order.getOrderCode(), order.getStatus()));
            }

            // Update order status
            order.setStatus(OrderStatus.COMPLETED);
            Order savedOrder = orderRepository.save(order);

            log.info("Order {} status updated to COMPLETED", savedOrder.getOrderCode());

            return new ResponseMessageDTO(HttpStatus.OK, "Thank you for your purchase! Your order has been completed.");

        } catch (IllegalArgumentException | IllegalStateException | NotFoundException e) {
            log.error("Business logic error in confirmOrderDelivered for order ID: {} and user ID: {}", orderId, userId,
                    e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in confirmOrderDelivered for order ID: {} and user ID: {}", orderId, userId, e);
            throw new RuntimeException("Failed to confirm order delivery. Please try again.", e);
        }
    }

    // --- Validation Methods ---

    private void validateOrderCreateRequest(Long userId, OrderCreateDTO createDTO, String authToken) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (createDTO == null) {
            throw new IllegalArgumentException("Order creation data cannot be null");
        }
        if (createDTO.getUserAddressId() == null) {
            throw new IllegalArgumentException("User address ID cannot be null");
        }
        if (createDTO.getPaymentMethodId() == null) {
            throw new IllegalArgumentException("Payment method ID cannot be null");
        }
        if (authToken == null || authToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Authentication token cannot be null or empty");
        }
    }

    private void validateCartItems(CartDetailDTO cart) {
        for (CartDetailDTO.CartItemDTO item : cart.getCartItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalStateException("Invalid quantity for product: " + item.getProductName());
            }
            if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("Invalid price for product: " + item.getProductName());
            }
            if (item.getProductVariantId() == null || item.getSizeId() == null) {
                throw new IllegalStateException(
                        "Missing product variant or size information for: " + item.getProductName());
            }
        }
    }

    // --- Private Helper Methods ---

    private Order findOrderForUser(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        if (!order.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this order.");
        }
        return order;
    }

    private Order buildOrder(Long userId, OrderCreateDTO createDTO, UserAddressDTO address, CartDetailDTO cart,
            PaymentMethod paymentMethod) {
        try {
            // Generate unique order code with retry mechanism
            String orderCode = generateUniqueOrderCode();

            Order order = Order.builder()
                    .orderCode(orderCode)
                    .userId(userId)
                    .status(OrderStatus.PENDING)
                    .paymentStatus(PaymentStatus.UNPAID)
                    .note(createDTO.getNote() != null ? createDTO.getNote().trim() : null)
                    .paymentMethodCode(paymentMethod.getCode())
                    .paymentMethodName(paymentMethod.getName())
                    .build();

            // Build shipping detail with validation
            OrderShippingDetail shippingDetail = OrderShippingDetail.builder()
                    .recipientName(address.getRecipientName() != null ? address.getRecipientName().trim() : "")
                    .phoneNumber(address.getPhoneNumber() != null ? address.getPhoneNumber().trim() : "")
                    .addressDetail(address.getAddressDetail() != null ? address.getAddressDetail().trim() : "")
                    .shippingFee(BigDecimal.ZERO) // Default shipping fee
                    .build();
            order.setShippingDetail(shippingDetail);

            // Build order items and calculate total
            BigDecimal totalPrice = BigDecimal.ZERO;
            for (CartDetailDTO.CartItemDTO cartItem : cart.getCartItems()) {
                // Validate cart item data
                if (cartItem.getProductName() == null || cartItem.getProductName().trim().isEmpty()) {
                    throw new IllegalStateException("Product name cannot be empty");
                }

                OrderItem orderItem = OrderItem.builder()
                        .productId(cartItem.getProductId())
                        .productVariantId(cartItem.getProductVariantId())
                        .sizeId(cartItem.getSizeId())
                        .productName(cartItem.getProductName().trim())
                        .colorName(cartItem.getColorName() != null ? cartItem.getColorName().trim() : "")
                        .sizeName(cartItem.getSizeName() != null ? cartItem.getSizeName().trim() : "")
                        .imageUrl(cartItem.getImageUrl())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getPrice())
                        .build();

                order.addOrderItem(orderItem);

                // Calculate subtotal for this item
                BigDecimal itemSubtotal = cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                totalPrice = totalPrice.add(itemSubtotal);

                log.debug("Added order item: {} x {} = {}", cartItem.getProductName(), cartItem.getQuantity(),
                        itemSubtotal);
            }

            order.setTotalPrice(totalPrice);
            log.info("Built order with code: {}, total items: {}, total price: {}",
                    orderCode, cart.getCartItems().size(), totalPrice);

            return order;

        } catch (Exception e) {
            log.error("Error building order for user ID: {}", userId, e);
            throw new RuntimeException("Failed to build order", e);
        }
    }

    private String generateUniqueOrderCode() {
        String orderCode;
        int attempts = 0;
        final int maxAttempts = 5;

        do {
            orderCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            attempts++;

            if (attempts > maxAttempts) {
                throw new RuntimeException("Unable to generate unique order code after " + maxAttempts + " attempts");
            }

        } while (orderRepository.existsByOrderCode(orderCode));

        return orderCode;
    }
}