package com.hotel_management.Hotel.config;

import com.hotel_management.Hotel.services.Custom.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AppConfig appConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/register",
                                "/users/login",
                                "/public/**").permitAll()

                        // USER Endpoints
                        .requestMatchers(
                                "/profile/**",
                                "/booking/**",
                                "/payment/**",
                                "/feedback/my/**",   // user can see their own feedback
                                "/feedback/add"      // user can add feedback
                        ).hasRole("USER")

                        // STAFF Endpoints
                        .requestMatchers(
                                "/users/**",
                                "/rooms/**",
                                "/booking/all",
                                "/booking/update/**",
                                "/payment/all",
                                "/payment/manual"
                        ).hasRole("STAFF")

                        // OWNER Endpoints
                        .requestMatchers(
                                "/analytics/**",
                                "/staff/**",
                                "/rooms/**",
                                "/booking/**",
                                "/payment/**",
                                "/users/**",
                                "/feedback/**"   // owner can view all feedback
                        ).hasRole("OWNER")

                        // DEVELOPER Endpoints
                        .requestMatchers(
                                "/system/**",
                                "/logs/**"
                        ).hasRole("DEVELOPER")

                        // Any other request is denied
                        .anyRequest().denyAll()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
