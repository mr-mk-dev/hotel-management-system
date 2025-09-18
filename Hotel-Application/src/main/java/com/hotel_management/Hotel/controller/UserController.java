package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.mapper.UserMapper;
import com.hotel_management.Hotel.services.Custom.CustomUserDetails;
import com.hotel_management.Hotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("user/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDTO requestDTO) {
        String message = userService.registerRequest(requestDTO);
        return ResponseEntity.ok(message);
    }

    @PostMapping("user/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp) {
        try {
            UserResponseDTO responseDTO = userService.verifyRegistration(email, otp);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String token = userService.verifyUser(user);

        if ("Fail".equals(token)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        User user1 = userService.findUserByEmail(user.getEmail());

        return ResponseEntity.ok(Map.of("token", token,"user", userMapper.toResponseDTO(user1)));
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        User user = userService.findUserByEmail(userDetails.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

}


