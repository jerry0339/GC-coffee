package com.example.gccoffee.repository;

import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderItemRepository {
    List<OrderItem> insertItems(Order order);

    List<OrderItem> findByOrderId(UUID orderId);

    void update(Order order);

    void delete(UUID orderId);

}
