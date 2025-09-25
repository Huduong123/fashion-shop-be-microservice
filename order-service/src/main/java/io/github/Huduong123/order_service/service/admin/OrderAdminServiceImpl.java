package io.github.Huduong123.order_service.service.admin;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.Huduong123.order_service.dto.admin.order.OrderAdminResponseDTO;
import io.github.Huduong123.order_service.dto.common.ResponseMessageDTO;
import io.github.Huduong123.order_service.entity.Order;
import io.github.Huduong123.order_service.entity.enums.OrderStatus;
import io.github.Huduong123.order_service.events.producer.OrderEventProducer;
import io.github.Huduong123.order_service.exception.NotFoundException;
import io.github.Huduong123.order_service.mapper.OrderMapper;
import io.github.Huduong123.order_service.repository.OrderRepository;
import io.github.Huduong123.order_service.specification.OrderSpecification;

@Service
public class OrderAdminServiceImpl implements OrderAdminService{

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;

    public OrderAdminServiceImpl(OrderEventProducer orderEventProducer, OrderMapper orderMapper, OrderRepository orderRepository) {
        this.orderEventProducer = orderEventProducer;
        this.orderMapper = orderMapper;
        this.orderRepository = orderRepository;
    }



    @Override
    @Transactional(readOnly = true)
    public Page<OrderAdminResponseDTO> getAllOrders(Pageable pageable, Long userId, OrderStatus status,
            LocalDate startDate, LocalDate endDate) {
        Specification<Order> spec = OrderSpecification.filterBy(userId, status, startDate, endDate);
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        return orderPage.map(orderMapper::toOrderAdminResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderAdminResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findByIdWithDetails(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        return orderMapper.toOrderAdminResponseDTO(order);
    }

    @Override
    @Transactional
    public ResponseMessageDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        // Thêm logic kiểm tra chuyển đổi trạng thái hợp lệ nếu cần
        // Ví dụ: không thể chuyển từ DELIVERED về PENDING
        
        order.setStatus(newStatus);
        orderRepository.save(order);
        
        // Nâng cao: Tại đây, bạn có thể phát ra một sự kiện như OrderStatusChangedEvent
        // để các service khác (ví dụ: notification-service) có thể hành động.
        // orderEventProducer.sendOrderStatusChangedEvent(...)

        return new ResponseMessageDTO(HttpStatus.OK, "Order status updated successfully to " + newStatus);
    }

    @Override
    public ResponseMessageDTO deleteOrderAndRestoreStock(Long orderId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteOrderAndRestoreStock'");
    }
    
}
