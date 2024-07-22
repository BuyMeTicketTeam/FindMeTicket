package com.booking.app.exceptions.user;


public class ForbiddenDeleteUserException extends RuntimeException {
    public ForbiddenDeleteUserException(String message) {
        super(message);
    }
}
