package io.github.Huduong123.order_service.mapper;


import io.github.Huduong123.order_service.entity.Order;
import io.github.Huduong123.order_service.entity.OrderItem;
import io.github.Huduong123.order_service.entity.OrderShippingDetail;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.github.Huduong123.order_service.dto.admin.order.OrderAdminResponseDTO;
import io.github.Huduong123.order_service.dto.admin.order.OrderItemAdminResponseDTO;
import io.github.Huduong123.order_service.dto.admin.order.OrderShippingDetailDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderItemResponseDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderResponseDTO;
import io.github.Huduong123.order_service.dto.user.order.OrderSummaryDTO;

@Component
public class OrderMapper {

    // ===================================================================
    // ======================= MAPPERS FOR ADMIN =========================
    // ===================================================================

    public OrderAdminResponseDTO toOrderAdminResponseDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItem> items = order.getOrderItems() != null ? order.getOrderItems() : Collections.emptyList();
        int totalItems = items.stream().mapToInt(OrderItem::getQuantity).sum();

        return OrderAdminResponseDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .totalItems(totalItems)
                .paymentMethodCode(order.getPaymentMethodCode())
                .paymentMethodName(order.getPaymentMethodName())
                .userId(order.getUserId()) // <-- THAY ĐỔI QUAN TRỌNG: Chỉ trả về ID
                .shippingDetail(toOrderShippingDetailDTO(order.getShippingDetail()))
                .orderItems(items.stream()
                        .map(this::toOrderItemAdminResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // ===================================================================
    // ======================== MAPPERS FOR USER =========================
    // ===================================================================

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        if (order == null) {
            return null;
        }
        
        List<OrderItem> items = order.getOrderItems() != null ? order.getOrderItems() : Collections.emptyList();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .note(order.getNote())
                .paymentMethodName(order.getPaymentMethodName())
                .paymentMethodCode(order.getPaymentMethodCode())
                .shippingDetail(toOrderShippingDetailDTO(order.getShippingDetail()))
                .orderItems(items.stream()
                        .map(this::toOrderItemResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public OrderSummaryDTO toOrderSummaryDTO(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItem> items = order.getOrderItems() != null ? order.getOrderItems() : Collections.emptyList();
        int totalItems = items.stream().mapToInt(OrderItem::getQuantity).sum();
        String representativeImage = items.isEmpty() ? null : items.get(0).getImageUrl();

        return OrderSummaryDTO.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .createdAt(order.getCreatedAt())
                .totalItems(totalItems)
                .representativeImage(representativeImage)
                .build();
    }

    // ===================================================================
    // ======================= HELPER MAPPERS (PRIVATE) ==================
    // ===================================================================

    private OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        BigDecimal subTotal = calculateSubTotal(orderItem.getPrice(), orderItem.getQuantity());

        return OrderItemResponseDTO.builder()
                .productId(orderItem.getProductId())
                .productVariantId(orderItem.getProductVariantId())
                .sizeId(orderItem.getSizeId())
                .productName(orderItem.getProductName())
                .colorName(orderItem.getColorName())
                .sizeName(orderItem.getSizeName())
                .imageUrl(orderItem.getImageUrl())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subTotal(subTotal)
                .build();
    }
    
    private OrderItemAdminResponseDTO toOrderItemAdminResponseDTO(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        BigDecimal subTotal = calculateSubTotal(orderItem.getPrice(), orderItem.getQuantity());

        return OrderItemAdminResponseDTO.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProductId())
                .productVariantId(orderItem.getProductVariantId())
                .sizeId(orderItem.getSizeId())
                .productName(orderItem.getProductName())
                .colorName(orderItem.getColorName())
                .sizeName(orderItem.getSizeName())
                .imageUrl(orderItem.getImageUrl())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .subTotal(subTotal)
                .build();
    }

    private OrderShippingDetailDTO toOrderShippingDetailDTO(OrderShippingDetail detail) {
        if (detail == null) {
            return null;
        }
        return OrderShippingDetailDTO.builder()
                .recipientName(detail.getRecipientName())
                .phoneNumber(detail.getPhoneNumber())
                .addressDetail(detail.getAddressDetail())
                .shippingFee(detail.getShippingFee())
                .build();
    }

    private BigDecimal calculateSubTotal(BigDecimal price, int quantity) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(new BigDecimal(quantity));
    }
}