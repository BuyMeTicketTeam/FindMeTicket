package com.booking.app.exceptions.base;

public class InvalidFormatException extends RuntimeException {
    private static final String MESSAGE = "%s must be in the format %s";

    public InvalidFormatException(String type, String pattern) {
        super(String.format(MESSAGE, type, pattern));
    }
}
