package com.hotel_management.Hotel.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String emailId;
    private String password;
}

