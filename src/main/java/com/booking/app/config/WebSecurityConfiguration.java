package com.booking.app.config;

import com.booking.app.security.CustomUserDetailsService;
import com.booking.app.security.jwt.JwtAuthFilter;
import com.booking.app.security.oauth2.*;
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
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Log4j2
public class WebSecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;

    private final JwtAuthFilter jwtAuthFilter;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.logout().disable();
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests().requestMatchers("/v3/api-docs/**", "/configuration/**", "/swagger*/**"
                        , "/webjars/**", "/auth/**", "/oauth2/**")
                .permitAll();
        http.authorizeHttpRequests()
                .requestMatchers("/register",
                        "/confirm-email",
                        "/resend-confirm-email",
                        "/confirm-email",
                        "/reset",
                        "/new-password",
                        "/login")
                .anonymous();
        http.authorizeHttpRequests().requestMatchers("/typeAhead").permitAll();
        http.authorizeHttpRequests().requestMatchers("/logout").authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint);

        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/googleAuth")
                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests().requestMatchers("/getCookie").permitAll();
        return http.build();
    }

    @Bean
    protected AuthenticationManager authenticationManager() {
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
