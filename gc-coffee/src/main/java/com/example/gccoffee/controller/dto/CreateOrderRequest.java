package com.example.gccoffee.controller.dto;

import com.example.gccoffee.model.OrderItem;
import java.util.List;

public record CreateOrderRequest(
  String email, String address, String postcode, List<OrderItem> orderItems
) {
}
