package com.booking.app.controller;

import com.booking.app.services.DeleteUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user deletion requests.
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Delete a user", description = "Deleting user from User Interface")
public class DeleteUserController {

    private final DeleteUserService deleteUserService;

    /**
     * Endpoint to delete a user.
     *
     * @param request  the HTTP request containing user information
     * @param response the HTTP response
     * @return ResponseEntity indicating the result of the deletion operation
     */
    @DeleteMapping("/delete-user")
    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden to delete user")
    })
    public ResponseEntity<?> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        return deleteUserService.deleteUser(request, response);
    }

}
