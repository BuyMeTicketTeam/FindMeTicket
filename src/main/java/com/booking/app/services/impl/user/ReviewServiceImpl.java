package com.booking.app.services.impl.user;

import com.booking.app.dto.users.CreateReviewDto;
import com.booking.app.dto.users.ReviewDto;
import com.booking.app.entities.user.Review;
import com.booking.app.entities.user.User;
import com.booking.app.exceptions.notfound.ReviewNotFoundException;
import com.booking.app.repositories.ReviewRepository;
import com.booking.app.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDto getReview(User user) {
        return Optional.ofNullable(user.getReview())
                .map(ReviewDto::createInstance)
                .orElseThrow(ReviewNotFoundException::new);
    }

    @Override
    public List<ReviewDto> getReviews() {
        List<Review> reviewList = reviewRepository.findAll();
        return reviewList.stream().map(ReviewDto::createInstance).toList();
    }

    @Override
    public ReviewDto saveReview(CreateReviewDto createReviewDto, User user) {
        Review newReview = Review.builder()
                .reviewText(createReviewDto.getReviewText())
                .grade(createReviewDto.getGrade())
                .user(user)
                .build();
        user.setReview(newReview);
        reviewRepository.save(newReview);

        return ReviewDto.createInstance(newReview);
    }

    @Override
    public void deleteReview(User user) {
        Optional.ofNullable(user.getReview())
                .ifPresent(reviewRepository::delete);
    }

}
