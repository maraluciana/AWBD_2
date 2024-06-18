package com.project.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import com.project.demo.model.Cart;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.demo.service.CartService;
import com.project.demo.dto.CartItemRequestDTO;

class CartControllerTest {
    @Test
    void testAddCartItemSuccess() {
        CartService cartServiceMock = mock(CartService.class);
        CartController cartController = new CartController(cartServiceMock);

        UUID cartId = UUID.randomUUID();
        CartItemRequestDTO cartItemDTO = new CartItemRequestDTO(UUID.randomUUID(), 2);

        Cart updatedCart = mock(Cart.class);

        when(cartServiceMock.addCartItem(eq(cartId), any())).thenReturn(updatedCart);

        ResponseEntity<String> response = cartController.addCartItem(cartId, cartItemDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cart item added successfully.", response.getBody());
    }

    @Test
    void testAddCartItemFailure() {
        CartService cartServiceMock = mock(CartService.class);
        CartController cartController = new CartController(cartServiceMock);

        UUID cartId = UUID.randomUUID();
        CartItemRequestDTO cartItemDTO = new CartItemRequestDTO(UUID.randomUUID(), 2);

        when(cartServiceMock.addCartItem(eq(cartId), any())).thenReturn(null);

        ResponseEntity<String> response = cartController.addCartItem(cartId, cartItemDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to add cart item. Cart may not be open or not found.", response.getBody());
    }

    @Test
    void testCreateCartSuccess() {
        CartService cartServiceMock = mock(CartService.class);
        CartController cartController = new CartController(cartServiceMock);

        UUID userId = UUID.randomUUID();
        Optional<Cart> createdCart = Optional.of(mock(Cart.class));

        when(cartServiceMock.createCart(userId)).thenReturn(createdCart);

        ResponseEntity<String> response = cartController.createCart(userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Cart created successfully.", response.getBody());
    }

    @Test
    void testCreateCartFailure() {
        CartService cartServiceMock = mock(CartService.class);
        CartController cartController = new CartController(cartServiceMock);

        UUID userId = UUID.randomUUID();
        Optional<Cart> createdCart = Optional.empty();

        when(cartServiceMock.createCart(userId)).thenReturn(createdCart);

        ResponseEntity<String> response = cartController.createCart(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to create cart. User not found.", response.getBody());
    }
}
