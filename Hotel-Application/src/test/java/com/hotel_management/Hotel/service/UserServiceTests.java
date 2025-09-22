package com.hotel_management.Hotel.service;

import com.hotel_management.Hotel.repository.UserRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepo userRepo;

    @Test
    void findById() {
        Assertions.assertNotNull(userRepo.findAll());
    }
}
