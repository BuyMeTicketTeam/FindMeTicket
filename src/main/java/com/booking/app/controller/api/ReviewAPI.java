package com.booking.app.controller.api;

import com.booking.app.dto.SaveReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

public interface ReviewAPI {

    @Operation(summary = "saving review of authorized user", description = "save review of authorized user")
    @ApiResponse(responseCode = "200", description = "saves review")
    ResponseEntity<?> saveReview(@NotNull @Valid SaveReviewDto saveReviewDto, HttpServletRequest request);

    @Operation(summary = "get all reviews", description = "get all reviews")
    @ApiResponse(responseCode = "200", description = "returns all reviews")
    ResponseEntity<?> getReviews();

    @Operation(summary = "delete review", description = "delete review of authorized user")
    @ApiResponse(responseCode = "200", description = "review deleted")
    @ApiResponse(responseCode = "404", description = "review not found")
    ResponseEntity<?> deleteReview(HttpServletRequest request);

    @Operation(summary = "get user review", description = "get authorized user review")
    @ApiResponse(responseCode = "200", description = "returns review")
    @ApiResponse(responseCode = "404", description = "review not present")
    ResponseEntity<?>getReview(HttpServletRequest request);
}
