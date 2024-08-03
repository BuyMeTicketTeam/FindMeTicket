package com.booking.app.exceptions.notfound;

public class ReviewNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Review was not found or user does not have review";

    public ReviewNotFoundException() {
        super(MESSAGE);
    }
}
