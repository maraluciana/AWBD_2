package com.project.demo.controller;

import com.project.demo.dto.OrderRequestDTO;
import com.project.demo.model.Order;
import com.project.demo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDTO orderRequest) {
        UUID userId = orderRequest.getUserId();
        UUID cartId = orderRequest.getCartId();
        Optional<Order> placedOrder = this.orderService.placeOrder(userId, cartId);

        if (placedOrder.isPresent()) {
            return new ResponseEntity<>("Order placed successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to place order. Cart may not be open or not found.", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable UUID userId) {
        List<Order> userOrders = this.orderService.getOrdersByUser(userId);
        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }
}
