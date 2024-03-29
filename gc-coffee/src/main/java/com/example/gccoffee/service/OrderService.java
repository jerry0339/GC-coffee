package com.example.gccoffee.service;

import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;

import com.example.gccoffee.model.OrderStatus;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);

    List<Order> getAllOrders();

    Order getOrderById(UUID orderId);

    Order updateOrder(UUID orderId, String address, String postcode, List<OrderItem> orderItems, OrderStatus orderStatus);

    void deleteOrder(UUID orderId);
}
