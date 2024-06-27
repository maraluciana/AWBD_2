package com.project.demo.exceptions;

public class FlowerNotFoundException extends RuntimeException {
    public FlowerNotFoundException(String flowerId) {
        super("Flower not found with ID: " + flowerId);
    }
}