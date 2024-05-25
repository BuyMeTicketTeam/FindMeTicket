package com.booking.app.exception.exception;


public class UserCredentialsNotFoundException extends RuntimeException {
    public static final String THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE = "The specified email is not registered";

    public UserCredentialsNotFoundException() {
        super(THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_MESSAGE);
    }
}
