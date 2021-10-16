package com.example.gccoffee.controller.api;

import com.example.gccoffee.controller.dto.CreateOrderRequest;
import com.example.gccoffee.controller.dto.UpdateOrderRequest;
import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.service.OrderService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderRestController {

  private final OrderService orderService;

  public OrderRestController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/api/v1/orders")
  public Order createOrder(@RequestBody CreateOrderRequest orderRequest) {
    return orderService.createOrder(
      new Email(orderRequest.email()),
      orderRequest.address(),
      orderRequest.postcode(),
      orderRequest.orderItems()
    );
  }

  @GetMapping("/api/v1/orders")
  public List<Order> getOrderList() {
    return orderService.getAllOrders();
  }

  @GetMapping("/api/v1/orders/{orderId}")
  public Order getOrderById(@PathVariable UUID orderId) {
    return orderService.getOrderById(orderId);
  }

  @PutMapping("/api/v1/orders/{orderId}")
  public Order updateOrder(
      @PathVariable UUID orderId,
      @RequestBody UpdateOrderRequest orderRequest) {

    return orderService.updateOrder(
        orderId,
        orderRequest.address(),
        orderRequest.postcode(),
        orderRequest.orderItems(),
        orderRequest.orderStatus());
  }

  @DeleteMapping("/api/v1/orders/{orderId}")
  public void deleteOrder(@PathVariable UUID orderId) {
    orderService.deleteOrder(orderId);
  }

}
