package com.booking.app.dto;

import com.booking.app.entity.Review;
import com.booking.app.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.ProviderNotFoundException;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    private UUID id;

    private String reviewText;

    private String writingDate;

    private int grade;

    private String username;

    private byte[] profilePicture;

    private String urlPicture;

    public static ReviewDTO createInstance(Review review) {
        User user = review.getUser();
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .id(user.getReview().getId())
                .writingDate(review.getWritingDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .grade(review.getGrade())
                .reviewText(review.getReviewText())
                .username(user.getSecurity().getUsername())
                .build();

        switch (user.getSecurity().getProvider()) {
            case GOOGLE -> reviewDTO.setUrlPicture(user.getUrlPicture());
            case LOCAL -> reviewDTO.setProfilePicture(user.getProfilePicture());
            default -> throw new ProviderNotFoundException("Third party service provider is not provided");
        }
        return reviewDTO;
    }
}
