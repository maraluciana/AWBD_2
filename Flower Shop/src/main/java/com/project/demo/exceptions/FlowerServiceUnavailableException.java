package com.project.demo.exceptions;

public class FlowerServiceUnavailableException extends RuntimeException {
    public FlowerServiceUnavailableException(String message) {
        super("Flower service is currently unavailable: " + message);
    }
}
