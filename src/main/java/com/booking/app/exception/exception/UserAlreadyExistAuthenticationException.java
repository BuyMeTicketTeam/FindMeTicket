package com.booking.app.exception.exception;

public class UserAlreadyExistAuthenticationException extends RuntimeException {
    public UserAlreadyExistAuthenticationException(String message) {
        super(message);
    }
}
