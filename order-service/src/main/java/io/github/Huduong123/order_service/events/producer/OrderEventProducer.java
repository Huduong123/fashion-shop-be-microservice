package io.github.Huduong123.order_service.events.producer;

import io.github.Huduong123.order_service.events.dto.OrderCancelledEvent;
import io.github.Huduong123.order_service.events.dto.OrderCreatedEvent;
import io.github.Huduong123.order_service.events.dto.OrderDeletedEvent;

public interface OrderEventProducer {
    void sendOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent);

    void sendOrderCancelledEvent(OrderCancelledEvent orderCancelledEvent);

    void sendOrderDeletedEvent(OrderDeletedEvent orderDeletedEvent);
}
