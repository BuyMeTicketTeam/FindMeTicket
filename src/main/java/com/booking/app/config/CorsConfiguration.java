package com.booking.app.config;

import com.booking.app.constant.CorsConfigConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfiguration {
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(CorsConfigConstants.ALLOWED_ORIGIN_80, CorsConfigConstants.ALLOWED_ORIGIN_81));
        configuration.setAllowedMethods(Arrays.asList(CorsConfigConstants.ALLOWED_METHODS));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfigConstants.ALLOWED_HEADERS));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList(CorsConfigConstants.EXPOSED_HEADER_AUTHORIZATION, CorsConfigConstants.EXPOSED_HEADER_REFRESH_TOKEN));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
