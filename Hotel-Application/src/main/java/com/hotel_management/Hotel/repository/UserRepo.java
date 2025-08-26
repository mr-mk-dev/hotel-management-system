package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);
}
