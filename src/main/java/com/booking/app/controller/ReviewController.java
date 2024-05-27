package com.booking.app.controller;

import com.booking.app.dto.ReviewDTO;
import com.booking.app.dto.SaveReviewDto;
import com.booking.app.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
@Tag(name = "Review", description = "Review management")
public class ReviewController {

    private final ReviewService reviewService;
    // TODO SECURE PATH
    @PostMapping("/reviews")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Save review", description = "Add user's review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully saved",
                    content = {@Content(schema = @Schema(implementation = ReviewDTO.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> saveReview(@RequestBody @NotNull @Valid SaveReviewDto saveReviewDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(reviewService.saveReview(saveReviewDto, request));
    }

    @GetMapping("/reviews")
    @Operation(summary = "Get reviews", description = "All reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully returned all reviews",
                    content = {@Content(schema = @Schema(implementation = ReviewDTO.class), mediaType = "application/json")})
    })
    public ResponseEntity<?> getReviews() {
        return ResponseEntity.ok().body(reviewService.getReviewList());
    }

    @DeleteMapping("/reviews")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Operation(summary = "Delete review", description = "Delete review of authorized user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Review is not found")
    })
    public ResponseEntity<?> deleteReview(HttpServletRequest request) {
        return reviewService.deleteReview(request) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/reviews")
    @Operation(summary = "Get user's review", description = "Get authorized user review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully returned review",
                    content = {@Content(schema = @Schema(implementation = ReviewDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Review is not found")
    })
    public ResponseEntity<?> getReview(HttpServletRequest request) {
        ReviewDTO reviewDTO = reviewService.getUserReview(request);
        return reviewDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(reviewDTO);
    }

}
