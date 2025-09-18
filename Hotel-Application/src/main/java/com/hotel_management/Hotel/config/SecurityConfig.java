package com.hotel_management.Hotel.config;

import com.hotel_management.Hotel.services.Custom.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
                """
                ROLE_OWNER > ROLE_STAFF
                ROLE_STAFF > ROLE_USER
                """);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(auth -> auth

                        // Public
                        .requestMatchers(
                                "/user/register" ,
                                "/user/login",
                                "/public/**").permitAll()
                        .requestMatchers("/profile/**").authenticated()
                        // USER Endpoints
                        .requestMatchers(
                                "/room/list",
                                "/room/find-type/**",
                                "/room/find-room/**",
                                "/room/find-amountgreater/**",
                                "/room/find-amountless/**",
                                "/room/find-between",
                                "/booking/add",
                                "/payment/bill",
                                "/payment/pay",
                                "/payment/latest",
                                "/feedback/my/**",   // user can see their own feedback
                                "/feedback/add/**",     // user can add feedback
                                "/feedback/update/**"      // user can add feedback
                        ).hasRole("USER")

                        // STAFF Endpoints (inherits USER endpoints)
                        .requestMatchers(
                                "/user/**",
                                "/room/**",
                                "/booking/**",
                                "/payment/**"
                        ).hasRole("STAFF")

                        // OWNER Endpoints (inherits STAFF + USER endpoints)
                        .requestMatchers(
                                "/analytics/**",
                                "/staff/**",
                                "/feedback/**"   // owner can view all feedback
                        ).hasRole("OWNER")

                        // DEVELOPER Endpoints
                        .requestMatchers(
                                "/system/**",
                                "/logs/**"
                        ).hasRole("DEVELOPER")

                        // Block everything else
                        .anyRequest().denyAll()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
