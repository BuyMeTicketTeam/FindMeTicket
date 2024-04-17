package com.booking.app.controller.api;

import com.booking.app.dto.ResponseDTO;
import com.booking.app.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
@Tag(name = "User operations")
public interface UserAPI {

    @Operation(summary = "Get user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is found")
    })
    ResponseDTO<UserDTO> getByID(@Parameter UUID id);

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users data")
    })
    ResponseDTO<List<UserDTO>> getAll();

    @Operation(summary = "Update user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been updated")
    })
    ResponseDTO<UserDTO> update(@Parameter UUID id, @NotNull UserDTO user);

    @Operation(summary = "Delete user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User has been deleted")

    })
    void delete(@Parameter UUID id);

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been created")
    })
    ResponseDTO<UserDTO> create(@NotNull UserDTO userDTO);

}
