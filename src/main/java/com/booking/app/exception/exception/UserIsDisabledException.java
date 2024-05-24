package com.booking.app.exception.exception;

public class UserIsDisabledException extends RuntimeException {
    private static final String MESSAGE_USER_IS_DISABLED = "User is not activated, try to sign up.";

    public UserIsDisabledException() {
        super(MESSAGE_USER_IS_DISABLED);
    }
}
