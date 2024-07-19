package com.booking.app.exceptions.user;

public class ReviewNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Review was not found or user does not have review";

    public ReviewNotFoundException() {
        super(MESSAGE);
    }
}
