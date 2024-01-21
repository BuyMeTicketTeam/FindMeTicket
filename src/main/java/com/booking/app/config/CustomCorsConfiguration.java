package com.booking.app.config;

import com.booking.app.constant.CustomHttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.booking.app.constant.CorsConfigConstants.*;

@Configuration
public class CustomCorsConfiguration extends CorsConfiguration {
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CustomCorsConfiguration configuration = new CustomCorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(ALLOWED_ORIGIN_80, ALLOWED_ORIGIN_81, ALLOWED_ORIGIN_FIND_ME_TICKET_80, ALLOWED_ORIGIN_FIND_ME_TICKET_81,ALLOWED_ORIGIN_LOCALHOST));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.CONTENT_LANGUAGE, HttpHeaders.ORIGIN, HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE, HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, HttpHeaders.CONTENT_TYPE));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.SET_COOKIE,
                CustomHttpHeaders.HEADER_USER_ID, CustomHttpHeaders.HEADER_REMEMBER_ME));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
