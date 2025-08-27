package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.config.SecurityConfig;
import com.hotel_management.Hotel.dto.LoginRequestDTO;
import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.mapper.UserMapper;
import com.hotel_management.Hotel.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserMapper userMapper;

    public UserService( UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        if (userRepository.existsByEmail(requestDTO.getEmail())) {   // checking phone
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByPhone(requestDTO.getPhone())) {  // checking email
            throw new IllegalArgumentException("Phone already in use");
        }

        User user = userMapper.fromReqToEntity(requestDTO);     // 1. Convert RequestDTO → Entity

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hashing

        User savedUser = userRepository.save(user);    // 2. Save Entity into DB
        System.out.println("User is :" + savedUser);

        return userMapper.toResponseDTO(savedUser);     // 3. Convert Entity → ResponseDTO
    }


    public boolean checkingUserLogin (LoginRequestDTO loginRequestDTO){
        Optional<User> email =
                userRepository.findByEmail(loginRequestDTO.getEmailId()) ;
        if(email.isPresent()) {
            User user = email.get();
            return passwordEncoder.matches(
                    loginRequestDTO.getPassword(),  // raw password from request
                    user.getPassword()              // hashed password stored
            );
        }
        return false;
    }

    public User findUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public boolean passwordValidator( String userPass,String dbPass ){
        return passwordEncoder.matches(
                userPass,
                dbPass );
    }

}
