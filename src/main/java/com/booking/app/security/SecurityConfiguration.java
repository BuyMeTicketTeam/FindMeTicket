package com.booking.app.security;

import com.booking.app.constant.CorsConfigConstants;
import com.booking.app.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Log4j2
public class SecurityConfiguration {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .requestMatchers("/register",
                        "/confirm-email",
                        "/resend-confirm-email",
                        "/confirm-email",
                        "/reset",
                        "/new-password",
                        "/login")
                .anonymous();
        http.authorizeHttpRequests().requestMatchers("/logout").authenticated();
        http.authorizeHttpRequests().requestMatchers("/get1").authenticated();

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors();
        http.logout().disable();
        http.csrf().disable();

        http.authorizeHttpRequests().requestMatchers("/getzxc").permitAll();

        return http.build();
    }

    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(List.of(daoAuthenticationProvider()));
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(CorsConfigConstants.allowedOrigin));
        configuration.setAllowedMethods(Arrays.asList(CorsConfigConstants.allowedMethods));
        configuration.setAllowedHeaders(Arrays.asList(CorsConfigConstants.allowedHeaders));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList(CorsConfigConstants.exposedHeaderAuthorization,CorsConfigConstants.exposedHeaderRefreshToken));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
