package com.booking.app.exception.exception;

public class UserIsNotFoundException extends RuntimeException {
    public UserIsNotFoundException(String message) {
        super(message);
    }
}
