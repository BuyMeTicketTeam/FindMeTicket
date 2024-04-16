package com.booking.app.controller;

import com.booking.app.controller.api.ReviewAPI;
import com.booking.app.dto.ReviewDTO;
import com.booking.app.dto.SaveReviewDto;
import com.booking.app.services.impl.ReviewServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@AllArgsConstructor
public class ReviewController implements ReviewAPI {

    private final ReviewServiceImpl reviewService;

    @PostMapping("/saveReview")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Override
    public ResponseEntity<?> saveReview(@RequestBody SaveReviewDto saveReviewDto, HttpServletRequest request) {
        return ResponseEntity.ok().body(reviewService.saveReview(saveReviewDto, request));
    }


    @GetMapping("/getReviews")
    @Override
    public ResponseEntity<?> getReviews() {
        return ResponseEntity.ok().body(reviewService.getReviewList());
    }

    @DeleteMapping("/deleteReview")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    @Override
    public ResponseEntity<?> deleteReview(HttpServletRequest request) {

        return reviewService.deleteReview(request) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/getUserReview")
    @Override
    public ResponseEntity<?> getReview(HttpServletRequest request) {

        ReviewDTO reviewDTO = reviewService.getUserReview(request);

        return reviewDTO == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(reviewDTO);
    }
}
