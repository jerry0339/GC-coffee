package com.example.gccoffee.service;

import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import com.example.gccoffee.model.OrderStatus;
import com.example.gccoffee.repository.OrderItemRepository;
import com.example.gccoffee.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultOrderService implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems) {
        Order order = new Order(
            UUID.randomUUID(),
            email,
            address,
            postcode,
            orderItems,
            OrderStatus.ACCEPTED,
            LocalDateTime.now(),
            LocalDateTime.now());
        orderRepository.insert(order);
        orderItemRepository.insertItems(order);
        return order;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.setOrderItems(orderItemRepository.findByOrderId(order.getOrderId()));
        }
        return orders;
    }

    @Override
    public Order getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderItems(orderItemRepository.findByOrderId(orderId));
        return order;
    }

    @Override
    @Transactional
    public Order updateOrder(UUID orderId, String address, String postcode, List<OrderItem> orderItems, OrderStatus orderStatus) {
        Order order = orderRepository.findById(orderId).get();
        order.setAddress(address);
        order.setPostcode(postcode);
        order.setOrderItems(orderItems);
        order.setOrderStatus(orderStatus);
        orderRepository.update(order);
        orderItemRepository.delete(orderId);
        orderItemRepository.insertItems(order);
        return order;
    }

    @Override
    @Transactional
    public void deleteOrder(UUID orderId) {
        orderRepository.delete(orderId);
        orderItemRepository.delete(orderId);
    }

}
