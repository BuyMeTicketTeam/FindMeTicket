package com.booking.app.services;

import com.booking.app.dto.ReviewDTO;
import com.booking.app.dto.SaveReviewDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ReviewService {

    ReviewDTO saveReview(SaveReviewDto saveReviewDto, HttpServletRequest request);

    List<ReviewDTO> getReviewList();

    boolean deleteReview(HttpServletRequest request);
}
