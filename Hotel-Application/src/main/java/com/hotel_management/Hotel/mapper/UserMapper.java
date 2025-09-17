package com.hotel_management.Hotel.mapper;

import com.hotel_management.Hotel.dto.UserRequestDTO;
import com.hotel_management.Hotel.dto.UserResponseDTO;
import com.hotel_management.Hotel.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User fromReqToEntity (UserRequestDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .role(dto.getRole())
                .password(dto.getPassword())
                .build();
    }

    // Entity → ResponseDTO (when sending data back)
    public UserResponseDTO toResponseDTO(User user) {
        if (user == null) return null;
        return UserResponseDTO.builder()
                .success(true)
                .message("User Created Successfully")
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
