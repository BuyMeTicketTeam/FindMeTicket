package com.booking.app.constant;

public class CorsConfigConstants {
    public static final String allowedOrigin = "http://build";
    public static final String[] allowedMethods = {"GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"};
    public static final String[] allowedHeaders = {"X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin","Access-Control-Max-Age","Refresh-Token"};
    public static final String allowCredentials = "true";
    public static final String exposedHeaderAuthorization = "Authorization";
    public static final String exposedHeaderRefreshToken = "Refresh-Token";
}
