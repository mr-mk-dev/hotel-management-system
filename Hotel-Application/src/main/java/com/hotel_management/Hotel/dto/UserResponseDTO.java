package com.hotel_management.Hotel.dto;

import com.hotel_management.Hotel.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Boolean success;
    private String message;
    private String id;
    private String name;
    private String email;
    private String phone;
    private Role role;
}

