package com.booking.app.exceptions.badrequest;

public class UndefinedSortingCriteriaException extends RuntimeException {
    private static final String MESSAGE = "Sorted criteria is not defined: %s";

    public UndefinedSortingCriteriaException(String criteria) {
        super(String.format(MESSAGE, criteria));
    }
}
