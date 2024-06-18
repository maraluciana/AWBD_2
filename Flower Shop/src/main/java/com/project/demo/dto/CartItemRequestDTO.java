package com.project.demo.dto;

import java.util.UUID;

public class CartItemRequestDTO {
    private UUID flowerId;
    private int quantity;

    public CartItemRequestDTO(UUID flowerId, int quantity){
        this.flowerId = flowerId;
        this.quantity = quantity;
    }

    public UUID getFlowerId() {
        return flowerId;
    }

    public void setFlowerId(UUID flowerId) {
        this.flowerId = flowerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
