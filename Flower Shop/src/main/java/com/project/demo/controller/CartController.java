package com.project.demo.controller;

import com.project.demo.dto.CartItemRequestDTO;
import com.project.demo.model.Cart;
import com.project.demo.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> createCart(@PathVariable UUID userId) {
        Optional<Cart> createdCart = cartService.createCart(userId);

        if (createdCart.isPresent()) {
            return new ResponseEntity<>("Cart created successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to create cart. User not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{cartId}/addItem")
    public ResponseEntity<String> addCartItem(@PathVariable UUID cartId, @RequestBody CartItemRequestDTO cartItemDTO) {
        Cart updatedCart = cartService.addCartItem(cartId, cartItemDTO);

        if (updatedCart != null) {
            return new ResponseEntity<>("Cart item added successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to add cart item. Cart may not be open or not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{cartId}/close")
    public ResponseEntity<String> closeCart(@PathVariable UUID cartId) {
        cartService.closeCart(cartId);
        return new ResponseEntity<>("Cart closed successfully.", HttpStatus.OK);
    }
}
