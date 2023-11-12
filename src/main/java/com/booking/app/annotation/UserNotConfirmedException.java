package com.booking.app.annotation;

public class UserNotConfirmedException extends RuntimeException {

    public UserNotConfirmedException(String message) {
        super(message);
    }
}