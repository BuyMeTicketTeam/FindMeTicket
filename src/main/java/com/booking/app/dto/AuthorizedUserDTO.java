package com.booking.app.dto;

import com.booking.app.entity.User;
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

    private String defaultAvatar;

    private String socialMediaAvatar;

    private Boolean notification;

    public static AuthorizedUserDTO createBasicAuthorizedUser(User user) {
        return AuthorizedUserDTO.builder()
                .email(user.getEmail())
                .notification(user.isNotification())
                .defaultAvatar(Base64.getEncoder().encodeToString(user.getDefaultAvatar()))
                .username(user.getUsername()).build();
    }

    public static AuthorizedUserDTO createGoogleAuthorizedUser(User user) {
        return AuthorizedUserDTO.builder()
                .email(user.getEmail())
                .notification(user.isNotification())
                .username(user.getUsername())
                .socialMediaAvatar(user.getSocialMediaAvatar()).build();
    }

}
