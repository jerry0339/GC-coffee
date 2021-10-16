package com.example.gccoffee.controller.dto;

import com.example.gccoffee.model.OrderItem;
import com.example.gccoffee.model.OrderStatus;
import java.util.List;

public record UpdateOrderRequest(
    String address, String postcode, List<OrderItem> orderItems, OrderStatus orderStatus
) {
}
