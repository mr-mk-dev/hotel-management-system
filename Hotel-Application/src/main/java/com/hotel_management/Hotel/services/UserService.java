package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.mapper.UserMapper;
import com.hotel_management.Hotel.repository.UserRepo;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private final UserMapper userMapper;

    private final Map<String, UserRequestDTO> pendingUsers = new ConcurrentHashMap<>();

    private final OtpService otpService;


    @Autowired
    public UserService( UserMapper userMapper,UserRepo userRepository, OtpService otpService) {
        this.otpService = otpService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public String registerUser(UserRequestDTO dto) {
        if (findUserByEmail(dto.getEmail()) != null) {
            return "Email already exists , Try log in now";
        }
        pendingUsers.put(dto.getEmail(), dto);
        return otpService.generateOtp(dto.getEmail(),dto.getName());
    }

    public boolean verifyUser(String email, String otp) {
        boolean isValid = otpService.validateOtp(email, otp);
        if (isValid) {
            UserRequestDTO dto = pendingUsers.remove(email);
            if (dto != null) {
                User user = userMapper.fromReqToEntity(dto);
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                saveUser(user);
            }
        }
        return isValid;
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
