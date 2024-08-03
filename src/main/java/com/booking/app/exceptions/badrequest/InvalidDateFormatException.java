package com.booking.app.exceptions.badrequest;

import com.booking.app.exceptions.base.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDateFormatException extends InvalidFormatException {

    public InvalidDateFormatException(String pattern) {
        super("Date", pattern);
    }
}
