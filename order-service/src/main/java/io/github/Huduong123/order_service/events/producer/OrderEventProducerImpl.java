package io.github.Huduong123.order_service.events.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import io.github.Huduong123.order_service.events.dto.OrderCancelledEvent;
import io.github.Huduong123.order_service.events.dto.OrderCreatedEvent;
import io.github.Huduong123.order_service.events.dto.OrderDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation của OrderEventProducer với Kafka integration.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducerImpl implements OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.order-events}")
    private String orderEventsTopic;

    @Override
    public void sendOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        try {
            log.info("Publishing OrderCreatedEvent: orderId={}, orderCode={}, userId={}",
                    orderCreatedEvent.getOrderId(),
                    orderCreatedEvent.getOrderCode(),
                    orderCreatedEvent.getUserId());

            // Send event to Kafka
            kafkaTemplate.send(orderEventsTopic, "order.created", orderCreatedEvent)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.debug("OrderCreatedEvent published successfully: {}", orderCreatedEvent);
                        } else {
                            log.error("Failed to publish OrderCreatedEvent: {}", orderCreatedEvent, ex);
                        }
                    });

        } catch (Exception e) {
            log.error("Failed to publish OrderCreatedEvent: {}", orderCreatedEvent, e);
        }
    }

    @Override
    public void sendOrderCancelledEvent(OrderCancelledEvent orderCancelledEvent) {
        try {
            // TODO: Implement actual message publishing với RabbitMQ/Kafka
            log.info("Publishing OrderCancelledEvent: orderId={}, orderCode={}, itemsToRestore={}",
                    orderCancelledEvent.getOrderId(),
                    orderCancelledEvent.getOrderCode(),
                    orderCancelledEvent.getItemsToRestore() != null ? orderCancelledEvent.getItemsToRestore().size()
                            : 0);

            // Stub implementation - log event
            log.debug("OrderCancelledEvent published successfully: {}", orderCancelledEvent);

            // Trong production, code sẽ như:
            // rabbitTemplate.convertAndSend("order.exchange", "order.cancelled",
            // orderCancelledEvent);
            // hoặc
            // kafkaTemplate.send("order-events", orderCancelledEvent);

        } catch (Exception e) {
            log.error("Failed to publish OrderCancelledEvent: {}", orderCancelledEvent, e);
            // Trong production có thể implement retry logic hoặc dead letter queue
        }
    }

    @Override
    public void sendOrderDeletedEvent(OrderDeletedEvent orderDeletedEvent) {
        try {
            // TODO: Implement actual message publishing với RabbitMQ/Kafka
            log.info("Publishing OrderDeletedEvent: orderId={}, itemsToRestore={}",
                    orderDeletedEvent.getOrderId(),
                    orderDeletedEvent.getItemsToRestore() != null ? orderDeletedEvent.getItemsToRestore().size() : 0);

            // Stub implementation - log event
            log.debug("OrderDeletedEvent published successfully: {}", orderDeletedEvent);

            // Trong production, code sẽ như:
            // rabbitTemplate.convertAndSend("order.exchange", "order.deleted",
            // orderDeletedEvent);
            // hoặc
            // kafkaTemplate.send("order-events", orderDeletedEvent);

        } catch (Exception e) {
            log.error("Failed to publish OrderDeletedEvent: {}", orderDeletedEvent, e);
            // Trong production có thể implement retry logic hoặc dead letter queue
        }
    }
}
