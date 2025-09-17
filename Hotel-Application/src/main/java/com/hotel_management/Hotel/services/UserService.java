package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.mapper.UserMapper;
import com.hotel_management.Hotel.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private final UserMapper userMapper;

    public UserService( UserMapper userMapper,UserRepo userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO requestDTO) {

        if (userRepository.existsByEmail(requestDTO.getEmail()) || userRepository.existsByPhone(requestDTO.getPhone()) ){   // checking phone
            return null;
        }

        User user = userMapper.fromReqToEntity(requestDTO);     // 1. Convert RequestDTO → Entity

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hashing

        User savedUser = userRepository.save(user);    // 2. Save Entity into DB

        return userMapper.toResponseDTO(savedUser);     // 3. Convert Entity → ResponseDTO
    }

    public UserResponseDTO saveUser(User user){
        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    public User findUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    public String verifyUser(User user){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getEmail());
        }
        return "Fail";
    }


}
