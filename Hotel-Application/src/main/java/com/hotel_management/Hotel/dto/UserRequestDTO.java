package com.hotel_management.Hotel.dto;

import com.hotel_management.Hotel.enums.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String password; // client can send this
}

