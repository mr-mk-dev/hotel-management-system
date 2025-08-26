package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.dto.LoginRequestDTO;
import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {
        UserResponseDTO responseDTO = userService.createUser(requestDTO);
        return ResponseEntity.ok(responseDTO); // send safe data back
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        if(userService.checkingUserLogin(loginRequestDTO))
            return ResponseEntity.ok("Login Successfully , Move to Room API");
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


