package com.hotel_management.Hotel.controller;


import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.mapper.UserMapper;
import com.hotel_management.Hotel.services.Custom.CustomUserDetails;
import com.hotel_management.Hotel.services.UserService;
import com.hotel_management.Hotel.services.email.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final OtpService otpService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private static final String USERNOTFOUND = "User not found";

    public UserController(UserService userService, UserMapper userMapper,OtpService otpService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.otpService = otpService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequestDTO userRequest) {
        String response = userService.registerUser(userRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/verify")
    public ResponseEntity<?> verifyUser(@RequestParam String email,
                                        @RequestParam String otp) {
        boolean isValid = userService.verifyUser(email, otp);
        if (isValid) {
            return ResponseEntity.ok("User registered successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP");
    }

    @PostMapping("user/login")
    public ResponseEntity<?> loginOtp(@RequestParam String email,@RequestParam String password) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USERNOTFOUND);
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        String response = otpService.generateOtpLogin(email,user.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/login-verify")
    public ResponseEntity<?> login(@RequestParam String email,@RequestParam String password, @RequestParam String otp) {

        boolean validate = otpService.validateOtpLogin(email,otp);
        if(!validate){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        }

        User user = userService.findUserByEmail(email);
        user.setPassword(password);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USERNOTFOUND);
        }

        return ResponseEntity.ok(userMapper.toResponseDTO(user));
    }

    @PutMapping("/user/profile/update-password")
    public ResponseEntity<?> updatePassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        User user = userService.findUserByEmail(userDetails.getUsername());
        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            return  ResponseEntity.ok().body("Password updated successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords don't match");
    }


}


