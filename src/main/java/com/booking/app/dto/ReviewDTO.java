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

    private byte[] defaultAvatar;

    private String socialMediaAvatar;

    public static ReviewDTO createInstance(Review review) {
        User user = review.getUser();
        ReviewDTO reviewDTO = ReviewDTO.builder()
                .id(user.getReview().getId())
                .writingDate(review.getAddingDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")))
                .grade(review.getGrade())
                .reviewText(review.getReviewText())
                .username(user.getUsername())
                .build();

        switch (user.getProvider()) {
            case GOOGLE -> reviewDTO.setSocialMediaAvatar(user.getSocialMediaAvatar());
            case LOCAL -> reviewDTO.setDefaultAvatar(user.getDefaultAvatar());
            default -> throw new ProviderNotFoundException("Third party service provider is not provided");
        }
        return reviewDTO;
    }
}
