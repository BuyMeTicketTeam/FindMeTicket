package com.booking.app.exception.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String s) {
        super(s);
    }
}
