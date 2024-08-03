package com.booking.app.exceptions.forbidden;


public class ForbiddenDeleteUserException extends RuntimeException {
    public ForbiddenDeleteUserException(String message) {
        super(message);
    }
}
