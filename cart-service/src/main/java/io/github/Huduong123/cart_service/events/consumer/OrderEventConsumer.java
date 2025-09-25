package io.github.Huduong123.cart_service.events.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import io.github.Huduong123.cart_service.events.dto.OrderCreatedEvent;
import io.github.Huduong123.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Kafka consumer để lắng nghe các events từ Order Service.
 * Khi có đơn hàng được tạo thành công, sẽ tự động xóa giỏ hàng của user đó.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final CartService cartService;

    @KafkaListener(topics = "${kafka.topic.order-events}", groupId = "${kafka.consumer.group-id}")
    public void handleOrderCreatedEvent(
            @Payload OrderCreatedEvent orderCreatedEvent,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        try {
            log.info(
                    "Received OrderCreatedEvent: userId={}, orderId={}, orderCode={} from topic={}, partition={}, offset={}",
                    orderCreatedEvent.getUserId(),
                    orderCreatedEvent.getOrderId(),
                    orderCreatedEvent.getOrderCode(),
                    topic, partition, offset);

            // Xóa giỏ hàng của user sau khi đơn hàng được tạo thành công
            cartService.clearCart(orderCreatedEvent.getUserId());

            log.info("Successfully cleared cart for user ID: {} after order creation",
                    orderCreatedEvent.getUserId());

        } catch (Exception e) {
            log.error("Failed to process OrderCreatedEvent: userId={}, orderId={}, orderCode={}",
                    orderCreatedEvent.getUserId(),
                    orderCreatedEvent.getOrderId(),
                    orderCreatedEvent.getOrderCode(), e);

            // In production, you might want to implement retry logic or send to DLQ
            // For now, we just log the error and continue
        }
    }
}
