package com.project.demo.exceptions;

public class InvalidFlowerException extends RuntimeException {
    public InvalidFlowerException(String message) {
        super("Invalid flower data: " + message);
    }
}