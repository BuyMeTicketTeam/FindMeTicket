package com.booking.app.exceptions.badrequest;


public class InvalidConfirmationCodeException extends RuntimeException {
    public static final String MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED = "Invalid confirmation code is provided";

    public InvalidConfirmationCodeException() {
        super(MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED);
    }
}
