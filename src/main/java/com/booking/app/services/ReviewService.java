package com.booking.app.services;

import com.booking.app.dto.users.CreateReviewDto;
import com.booking.app.dto.users.ReviewDto;
import com.booking.app.entities.user.User;
import com.booking.app.exceptions.notfound.ReviewNotFoundException;

import java.util.List;

/**
 * Service interface for handling review-related operations.
 */
public interface ReviewService {

    /**
     * Retrieves the review for a given user.
     *
     * @param user the user whose review is to be retrieved
     * @return the review of the user wrapped in a ReviewDto
     * @throws ReviewNotFoundException if the user does not have a review
     */
    ReviewDto getReview(User user);

    /**
     * Retrieves all reviews.
     *
     * @return a list of all reviews wrapped in ReviewDto objects
     */
    List<ReviewDto> getReviews();

    /**
     * Saves a new review for a given user.
     *
     * @param createReviewDto the data transfer object containing the details of the review to be created
     * @param user            the user for whom the review is to be created
     * @return the created review wrapped in a ReviewDto
     */
    ReviewDto saveReview(CreateReviewDto createReviewDto, User user);

    /**
     * Deletes the review of a given user.
     *
     * @param user the user whose review is to be deleted
     */
    void deleteReview(User user);

}
