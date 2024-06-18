package com.project.demo.dto;

import java.util.UUID;

public class OrderRequestDTO {
    private UUID userId;
    private UUID cartId;

    public OrderRequestDTO(UUID userId, UUID cartId){
        this.userId = userId;
        this.cartId = cartId;
    }
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }
}
