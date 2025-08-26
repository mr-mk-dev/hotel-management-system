package com.hotel_management.Hotel.config;

import com.hotel_management.Hotel.services.Custom.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
   public AuthenticationProvider authenticationProvider(){
       DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
       provider.setUserDetailsService(customUserDetailsService);
       provider.setPasswordEncoder(appConfig.passwordEncoder());
       return provider;
   }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/users/register",
                                "/users/login").permitAll()
                        .requestMatchers("/booking/**", "/profile/**").hasRole("USER")
                        .requestMatchers("/staff/**", "/checkin/**", "/checkout/**").hasRole("STAFF")
                        .requestMatchers("/dev/**").hasRole("DEV")
                        .requestMatchers("/admin/**", "/reports/**", "/manage/**").hasRole("OWNER")
                        .requestMatchers("/dashboard/**").hasAnyRole("STAFF", "OWNER")
                        .anyRequest().authenticated()     // allow all requests
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
