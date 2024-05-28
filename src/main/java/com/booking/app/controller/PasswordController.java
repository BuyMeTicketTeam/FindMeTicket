package com.booking.app.controller;

import com.booking.app.dto.PasswordDto;
import com.booking.app.dto.RequestUpdatePasswordDTO;
import com.booking.app.entity.User;
import com.booking.app.exception.ErrorDetails;
import com.booking.app.services.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booking.app.constant.PasswordConstantMessages.*;
import static com.booking.app.exception.exception.InvalidConfirmationCodeException.MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED;

@RestController
@RequestMapping
@AllArgsConstructor
@Tag(name = "Password", description = "Endpoints over password operations")
public class PasswordController {

    private final PasswordService passwordService;


    @PatchMapping("/users/password/reset")
    @Operation(summary = "Create new password", description = "Confirm code and set new password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET),
            @ApiResponse(responseCode = "400",
                    description = MESSAGE_INVALID_CONFIRMATION_CODE_IS_PROVIDED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = THE_SPECIFIED_EMAIL_IS_NOT_REGISTERED_OR_THE_ACCOUNT_IS_DISABLED,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> confirmResetPassword(@RequestBody @NotNull @Valid PasswordDto dto) {
        passwordService.resetPassword(dto);
        return ResponseEntity.ok(MESSAGE_PASSWORD_HAS_BEEN_SUCCESSFULLY_RESET);
    }


    // todo {userId}
    // *authentication required
    @PatchMapping("/users/password/update")
    @Operation(summary = "Update password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MESSAGE_NEW_PASSWORD_HAS_BEEN_CREATED),
            @ApiResponse(responseCode = "400",
                    description = MESSAGE_CURRENT_PASSWORD_IS_NOT_RIGHT,
                    content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> updatePassword(@RequestBody @NotNull @Valid RequestUpdatePasswordDTO dto,
                                            @AuthenticationPrincipal User user) {
        passwordService.changePassword(dto, user);
        return ResponseEntity.ok().body(MESSAGE_NEW_PASSWORD_HAS_BEEN_CREATED);
    }

}
