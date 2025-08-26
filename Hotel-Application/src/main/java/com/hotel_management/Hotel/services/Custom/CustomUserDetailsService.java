package com.hotel_management.Hotel.services.Custom;

import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws IllegalArgumentException {

        Optional<User> user = userRepository.findByEmail(email);

        return new CustomUserDetails(user.orElse(null));

    }
}

