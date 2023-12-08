package com.booking.app.constant;

public class CorsConfigConstants {
    public static final String ALLOWED_ORIGIN_80 = "http://build";
    public static final String ALLOWED_ORIGIN_81 = "http://build:81";
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"};
    public static final String[] ALLOWED_HEADERS = {"X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Max-Age", "Refresh-Token"};
    public static final String ALLOW_CREDENTIALS = "true";
    public static final String EXPOSED_HEADER_AUTHORIZATION = "Authorization";
    public static final String EXPOSED_HEADER_REFRESH_TOKEN = "Refresh-Token";

    private CorsConfigConstants() {
    }
}
