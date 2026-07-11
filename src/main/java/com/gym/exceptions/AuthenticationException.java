package com.gym.exceptions;

//exception handler when authentication fails
public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message) {
        super(message);
    }
}
