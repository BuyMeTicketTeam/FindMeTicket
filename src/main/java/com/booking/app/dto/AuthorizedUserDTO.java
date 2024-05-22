package com.booking.app.dto;

import com.booking.app.entity.UserCredentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorizedUserDTO {
    private String username;

    private String email;

    private String basicPicture;

    private String googlePicture;

    private Boolean notification;

    public static AuthorizedUserDTO createBasicAuthorizedUser(UserCredentials userCredentials) {
        return AuthorizedUserDTO.builder()
                .email(userCredentials.getEmail())
                .notification(userCredentials.getUser().isNotification())
                .basicPicture(Base64.getEncoder().encodeToString(userCredentials.getUser().getProfilePicture()))
                .username(userCredentials.getUsername()).build();
    }

    public static AuthorizedUserDTO createGoogleAuthorizedUser(UserCredentials userCredentials) {
        return AuthorizedUserDTO.builder()
                .email(userCredentials.getEmail())
                .notification(userCredentials.getUser().isNotification())
                .username(userCredentials.getUsername())
                .googlePicture(userCredentials.getUser().getUrlPicture()).build();
    }

}
