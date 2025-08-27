package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.createUser(requestDTO);
        return ResponseEntity.ok(responseDTO); // send safe data back
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(
            @RequestHeader ("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        // Decode Base64 -> "email:password"
        String base64Credentials = authHeader.substring("Basic ".length());
        String credentials =
                new String(Base64.getDecoder().decode(base64Credentials));
        String[] values = credentials.split(":", 2);

        String email = values[0];
        String rawPassword = values[1];

        User user = userService.findUserByEmail(email);

        if(user == null) throw new RuntimeException("User not found");

        // Verify password with BCrypt
        if (!userService.passwordValidator(rawPassword, user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        return ResponseEntity.ok("Login successful! Welcome " + user.getName());
    }

    @GetMapping("/staff/profile")
    public String returnVal (){
        return "Returning /staff/profile";
    }

    @GetMapping("/checkin")
    public String returnVal2 (){
        return "Returning /checkin";
    }
    @GetMapping("/checkout")
    public String returnVal3 (){
        return "Returning /checkout";
    }

    @GetMapping("/admin")
    public String returnVal4 (){
        return "Returning /admin";
    }
    @GetMapping("/report")
    public String returnVal5 (){
        return "Returning /report";
    }
    @GetMapping("/manage")
    public String returnVal6 (){
        return "Returning /manage";
    }


}


