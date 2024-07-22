package com.booking.app.exceptions.user;

public class LastPasswordIsNotRightException extends RuntimeException {
    private static final String MESSAGE_CURRENT_PASSWORD_IS_NOT_RIGHT = "We're sorry but you your current password is different.";

    public LastPasswordIsNotRightException() {
        super(MESSAGE_CURRENT_PASSWORD_IS_NOT_RIGHT);
    }
}
