package com.project.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import com.project.demo.dto.OrderRequestDTO;
import com.project.demo.model.Order;
import com.project.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OrderControllerTest {

    @Test
    void testPlaceOrderSuccess() {
        OrderService orderServiceMock = mock(OrderService.class);
        OrderController orderController = new OrderController(orderServiceMock);

        UUID userId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        OrderRequestDTO orderRequest = new OrderRequestDTO(userId, cartId);

        Optional<Order> placedOrder = Optional.of(new Order());

        when(orderServiceMock.placeOrder(eq(userId), eq(cartId))).thenReturn(placedOrder);

        ResponseEntity<String> response = orderController.placeOrder(orderRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order placed successfully.", response.getBody());
    }

    @Test
    void testPlaceOrderFailure() {
        OrderService orderServiceMock = mock(OrderService.class);
        OrderController orderController = new OrderController(orderServiceMock);

        UUID userId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        OrderRequestDTO orderRequest = new OrderRequestDTO(userId, cartId);

        Optional<Order> placedOrder = Optional.empty();

        when(orderServiceMock.placeOrder(eq(userId), eq(cartId))).thenReturn(placedOrder);

        ResponseEntity<String> response = orderController.placeOrder(orderRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to place order. Cart may not be open or not found.", response.getBody());
    }
}
