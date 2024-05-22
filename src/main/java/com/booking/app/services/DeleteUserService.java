package com.booking.app.services;

import com.booking.app.exception.exception.ForbiddenDeleteUserException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface DeleteUserService {

    /**
     * Deletes a user based on the request and response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return a ResponseEntity indicating the outcome of the delete operation
     * @throws ForbiddenDeleteUserException if the data in cookies don't match or are invalid
     */
    ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response) throws ForbiddenDeleteUserException;
}
