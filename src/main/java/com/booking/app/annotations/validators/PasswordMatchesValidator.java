package com.booking.app.annotations.validators;

import com.booking.app.annotations.PasswordMatches;
import com.booking.app.exceptions.badrequest.PasswordNotMatchesException;
import com.booking.app.utils.ConfirmPasswordUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String defaultValidationMessage;

    @Override
    public void initialize(PasswordMatches passwordMatches) {
        defaultValidationMessage = passwordMatches.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        ConfirmPasswordUtils userDto = (ConfirmPasswordUtils) obj;
        if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return true;
        }
        throw new PasswordNotMatchesException(defaultValidationMessage);
    }

}
