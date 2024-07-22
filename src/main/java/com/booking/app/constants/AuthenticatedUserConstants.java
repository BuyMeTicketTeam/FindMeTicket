package com.booking.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticatedUserConstants {
    public static final String REFRESH_TOKEN = "RefreshToken";
    public static final String BEARER = "Bearer ";
    public static final String USER_ID = "USER_ID";
    public static final String REMEMBER_ME = "RememberMe";
}
