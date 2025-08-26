package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.FoodOrder;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface FoodOrderRepo extends
        MongoRepository<FoodOrder,String> {
}

