package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.mapper.UserMapper;
import com.hotel_management.Hotel.services.Custom.CustomUserDetails;
import com.hotel_management.Hotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.createUser(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        String token = userService.verifyUser(loginRequest);

        if ("Fail".equals(token)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }



}


