package com.booking.app.security;

import com.booking.app.security.jwt.JwtAuthFilter;
import com.booking.app.services.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

        http.csrf().disable();
        //http.cors();

        http.authorizeHttpRequests()
                .requestMatchers("/register",
                        "/confirm-email",
                        "/resend-confirm-email",
                        "/confirm-email",
                        "/reset",
                        "/new-password",
                        "/login")
                .anonymous();
                //.requestMatchers(HttpMethod.POST, "/register").permitAll()



        http.logout(logout -> logout.logoutUrl("/logout").clearAuthentication(true).deleteCookies("refreshToken").deleteCookies("remember-me"));
        http.rememberMe(rememberme -> rememberme.userDetailsService(userDetailsService).tokenValiditySeconds(2592000));
//                .requestMatchers("/login").hasAnyRole("ROLE_USER", "ROLE_ANONYMOUS")
//                .requestMatchers("/login").hasAnyAuthority("ROLE_USER", "ROLE_ANONYMOUS")
//                .requestMatchers("/login").hasAnyAuthority("USER", "ANONYMOUS");
//                .and().formLogin().loginPage("/login").defaultSuccessUrl("/landingPage", true).failureUrl("/login?error=true").usernameParameter(
//                        "email").passwordParameter("password").and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").and()
//                .rememberMe().rememberMeCookieName("REMEMBER_ME").rememberMeParameter("remember_me").tokenValiditySeconds(123456).key(
//                        "49874795145977617241")
//                .and().exceptionHandling().accessDeniedPage("/err/403").and();
        //http.formLogin().loginPage("/login").failureHandler();

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));



        return http.build();

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
        configuration.setAllowedOrigins(Arrays.asList("http://build"));
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        configuration.setAllowedHeaders(
                Arrays.asList(
                        "X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization","Set-Cookie", "Access-Control-Allow-Origin"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);


        log.info("faaaa" + configuration.getAllowCredentials());
        log.info(configuration.getAllowedOrigins());
        log.info(configuration.getAllowedMethods());
        return source;
    }


    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(List.of(daoAuthenticationProvider()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        MessagePackFactory messagePackFactory = new MessagePackFactory();
//        messagePackFactory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
//        return new ObjectMapper(messagePackFactory);
//
//    }

}
