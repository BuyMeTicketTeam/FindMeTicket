package com.booking.app.services.impl;

import com.booking.app.dto.ReviewDTO;
import com.booking.app.dto.SaveReviewDto;
import com.booking.app.entity.Review;
import com.booking.app.entity.User;
import com.booking.app.entity.UserCredentials;
import com.booking.app.repositories.ReviewRepository;
import com.booking.app.repositories.UserCredentialsRepository;
import com.booking.app.repositories.UserRepository;
import com.booking.app.services.ReviewService;
import com.booking.app.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.booking.app.constant.CustomHttpHeaders.USER_ID;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public ReviewDTO saveReview(SaveReviewDto saveReviewDto, HttpServletRequest request) {

        Optional<UUID> uuid = CookieUtils.getCookie(request, USER_ID).map(cookie -> UUID.fromString(cookie.getValue()));

        Optional<UserCredentials> userCredentials = uuid.flatMap(userCredentialsRepository::findById);

        if (userCredentials.isPresent()) {
            User user = userCredentials.get().getUser();

            if (user.getReview() != null) {
                reviewRepository.delete(user.getReview());
            }
            Review review = Review.builder()
                    .reviewText(saveReviewDto.getReviewText())
                    .grade(saveReviewDto.getGrade())
                    .user(user).build();
            user.setReview(review);
            reviewRepository.save(review);
            return ReviewDTO.createInstance(review);
        } else return null;

    }

    @Override
    public List<ReviewDTO> getReviewList() {

        List<Review> reviewList = reviewRepository.findAll();

        return reviewList.stream().map(ReviewDTO::createInstance).toList();
    }

    @Override
    public boolean deleteReview(HttpServletRequest request) {

        Optional<UUID> uuid = CookieUtils.getCookie(request, USER_ID).map(cookie -> UUID.fromString(cookie.getValue()));

        Optional<UserCredentials> userCredentials = uuid.flatMap(userCredentialsRepository::findById);

        if (userCredentials.isPresent()) {
            if (userCredentials.get().getUser().getReview() != null) {
                reviewRepository.delete(userCredentials.get().getUser().getReview());
                return true;
            }
        }
        return false;
    }
}
