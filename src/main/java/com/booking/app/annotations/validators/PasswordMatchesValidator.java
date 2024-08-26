package com.booking.app.annotations.validators;

import com.booking.app.annotations.PasswordMatches;
import com.booking.app.exceptions.badrequest.PasswordNotMatchesException;
import com.booking.app.utils.ConfirmPasswordUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String defaultValidationMessage;

    @Override
    public void initialize(PasswordMatches passwordMatches) {
        defaultValidationMessage = passwordMatches.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            log.error("Object with password field is null");
            return true;
        }

        ConfirmPasswordUtils userDto = (ConfirmPasswordUtils) obj;
        String password = userDto.getPassword();
        String confirmPassword = userDto.getConfirmPassword();

        if (password == null || confirmPassword == null) {
            log.warn("Password or confirm password is null");
            return false;
        } else if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return true;
        } else {
            throw new PasswordNotMatchesException(defaultValidationMessage);
        }
    }

}
