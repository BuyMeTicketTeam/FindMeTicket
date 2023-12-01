package com.booking.app.constant;

public class CorsConfigConstants {
    public static final String allowedOrigin = "http://build";
    public static final String[] allowedMethods = {"GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"};
    public static final String[] AllowedHeaders = {"X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization","Set-Cookie", "Access-Control-Allow-Origin", "Remember-Me","Access-Control-Max-Age"};
    public static final String allowCredentials = "true";
    public static final String exposedHeaderSetCookie = "Set-Cookie";
    public static final String exposedHeaderAuthorization = "Authorization";
    public static final String exposedHeaderMaxAge = "Access-Control-Max-Age";
}
