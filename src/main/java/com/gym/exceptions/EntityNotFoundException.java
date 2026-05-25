package com.gym.exceptions;

//exception handler when entity is not found
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
