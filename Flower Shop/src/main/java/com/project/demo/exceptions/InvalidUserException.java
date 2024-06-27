package com.project.demo.exceptions;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super("Invalid user data: " + message);
    }
}