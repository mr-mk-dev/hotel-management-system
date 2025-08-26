package com.hotel_management.Hotel.repository;


import com.hotel_management.Hotel.entity.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuItemRepo extends MongoRepository<MenuItem,String> {
}

