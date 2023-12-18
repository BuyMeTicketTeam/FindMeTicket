package com.booking.app.util;

import com.booking.app.entity.UserSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationUtils {
    public static String extractEmailFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (userDetails instanceof UserSecurity) {
                UserSecurity userSecurity = (UserSecurity) userDetails;
                return userSecurity.getEmail();
            } else {
            }
        }

        return null;
    }
}
