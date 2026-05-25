package com.gym.exceptions;

//exception handler when validation fails
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
