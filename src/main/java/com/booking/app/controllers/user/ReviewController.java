package com.booking.app.controllers.user;

import com.booking.app.annotations.GlobalApiResponses;
import com.booking.app.constants.ApiMessagesConstants;
import com.booking.app.dto.CreateReviewDto;
import com.booking.app.dto.ReviewDto;
import com.booking.app.entities.user.User;
import com.booking.app.exceptions.ErrorDetailsDto;
import com.booking.app.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Review management")
@GlobalApiResponses
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{userId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Add review",
            description = "Adds user review",
            parameters = {
                    @Parameter(name = HttpHeaders.AUTHORIZATION, in = ParameterIn.HEADER, required = true, description = "Provide access JWT token",
                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
                    @Parameter(name = "RefreshToken", in = ParameterIn.COOKIE, required = true, description = "Provide refresh JWT token",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
            },
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Saves and returns review",
                            content = {@Content(schema = @Schema(implementation = ReviewDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),

                    @ApiResponse(responseCode = "401", description = ApiMessagesConstants.UNAUTHENTICATED_MESSAGE, content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            }
    )
    public ReviewDto saveReview(@PathVariable("userId") String userId,
                                @RequestBody @NotNull @Valid CreateReviewDto createReviewDto,
                                @AuthenticationPrincipal User user) {
        return reviewService.saveReview(createReviewDto, user);
    }

    @GetMapping("/reviews")
    @Operation(summary = "Get all reviews",
            description = "Gets all users reviews",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully returned all reviews",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReviewDto.class)), mediaType = MediaType.APPLICATION_JSON_VALUE))
            }
    )
    public List<ReviewDto> getAllReviews() {
        return reviewService.getReviews();
    }


    @GetMapping("/{userId}/reviews")
    @Operation(summary = "Get user review",
            description = "Gets authorized user review",
            parameters = {
                    @Parameter(name = HttpHeaders.AUTHORIZATION, in = ParameterIn.HEADER, required = true, description = "Provide access JWT token",
                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
                    @Parameter(name = "RefreshToken", in = ParameterIn.COOKIE, required = true, description = "Provide refresh JWT token",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
            },
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully returned review",
                            content = {@Content(schema = @Schema(implementation = ReviewDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
                    @ApiResponse(responseCode = "404", description = "Review is not found")
            }
    )
    public ReviewDto getReview(@PathVariable("userId") String userId,
                               @AuthenticationPrincipal User user) {
        return reviewService.getReview(user);
    }

    @DeleteMapping("/{userId}/reviews")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Delete review",
            description = "Deletes review of authorized user",
            parameters = {
                    @Parameter(name = HttpHeaders.AUTHORIZATION, in = ParameterIn.HEADER, required = true, description = "Provide access JWT token",
                            schema = @Schema(type = "string", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
                    @Parameter(name = "RefreshToken", in = ParameterIn.COOKIE, required = true, description = "Provide refresh JWT token",
                            schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")),
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted"),
                    @ApiResponse(responseCode = "401", description = ApiMessagesConstants.UNAUTHENTICATED_MESSAGE, content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class), mediaType = MediaType.APPLICATION_JSON_VALUE))
            }
    )
    public void deleteReview(@PathVariable("userId") String userId,
                             @AuthenticationPrincipal User user) {
        reviewService.deleteReview(user);
    }

}
