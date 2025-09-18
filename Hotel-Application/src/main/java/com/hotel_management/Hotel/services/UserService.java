package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.mapper.UserMapper;
import com.hotel_management.Hotel.repository.UserRepo;
import com.hotel_management.Hotel.services.email.MailService;
import com.hotel_management.Hotel.services.email.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final UserRepo userRepository;

    @Autowired
    private MailService  mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private OtpService  otpService;

    private final UserMapper userMapper;

    private final Map<String, UserRequestDTO> pendingUsers = new ConcurrentHashMap<>();

    public UserService( UserMapper userMapper,UserRepo userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public String registerRequest(UserRequestDTO requestDTO) {
        if (userRepository.existsByEmail(requestDTO.getEmail()) ||
                userRepository.existsByPhone(requestDTO.getPhone())) {
            return "User with email/phone already exists!";
        }

        pendingUsers.put(requestDTO.getEmail(), requestDTO);
        return otpService.generateOtp(requestDTO.getEmail());
    }

    public UserResponseDTO verifyRegistration(String email, String otp) {
        UserRequestDTO requestDTO = pendingUsers.get(email);

        if (requestDTO == null) {
            throw new RuntimeException("No pending registration for this email");
        }

        boolean isValid = otpService.validateOtp(email, otp);

        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = userMapper.fromReqToEntity(requestDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        pendingUsers.remove(email);
        return userMapper.toResponseDTO(savedUser);
    }


    public UserResponseDTO saveUser(User user){
        userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
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
