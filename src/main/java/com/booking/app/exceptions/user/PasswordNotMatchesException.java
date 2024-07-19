package com.booking.app.exceptions.user;

import jakarta.validation.ConstraintDeclarationException;

public class PasswordNotMatchesException extends ConstraintDeclarationException {

    public PasswordNotMatchesException(String message) {
        super(message);
    }

}
