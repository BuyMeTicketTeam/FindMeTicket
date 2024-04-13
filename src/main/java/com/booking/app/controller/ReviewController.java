package com.booking.app.controller;

import com.booking.app.controller.api.ReviewAPI;
import com.booking.app.dto.SaveReviewDto;
import com.booking.app.services.impl.ReviewServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class ReviewController implements ReviewAPI {

    private final ReviewServiceImpl reviewService;

    @PostMapping("/saveReview")
    @PreAuthorize("#{hasAnyRole('USER', 'ADMIN')}")
    public ResponseEntity<?> saveReview(SaveReviewDto saveReviewDto, HttpServletRequest request){

        reviewService.saveReview(saveReviewDto, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getReviews")
    public ResponseEntity<?>getReviews(){
        return ResponseEntity.ok().body(reviewService.getReviewList());
    }
}
