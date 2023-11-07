package com.booking.app.annotation;

import com.booking.app.controller.dto.RegistrationDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches passwordMatches) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        RegistrationDTO userDto = (RegistrationDTO) obj;
        return userDto.getPassword().equals(userDto.getConfirmPassword());
    }
}