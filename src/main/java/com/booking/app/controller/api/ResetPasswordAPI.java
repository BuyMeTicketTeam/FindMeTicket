package com.booking.app.controller.api;

import com.booking.app.dto.EmailDTO;
import com.booking.app.dto.ResetPasswordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
@Tag(name = "Resetting password", description = "Endpoints for resetting and confirming new password")
public interface ResetPasswordAPI {

    @Operation(summary = "Send password reset token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Reset token has been sent")
    })
    ResponseEntity<?> sendResetToken(@NotNull @Valid EmailDTO dto, String siteLanguage) throws MessagingException;

    @Operation(summary = "Confirmation reset password token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password has been reset"),
            @ApiResponse(responseCode = "400", description = "Token from email is not right")
    })
    ResponseEntity<?> confirmResetPassword(@NotNull @Valid ResetPasswordDTO dto);

}
