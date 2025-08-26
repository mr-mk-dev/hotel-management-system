package com.hotel_management.Hotel.entity;

import com.hotel_management.Hotel.enums.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;  // It will be given by MongoDB

    private String name;  // Will store Full name

    private String email; // It also will be unique

    private String phone; // It should be 10 digit and unique

    private Role role;  // Example - CUSTOMER, STAFF, OWNER , DEVELOPER

    private String password; //We will encrypt it later using Spring Security
}
