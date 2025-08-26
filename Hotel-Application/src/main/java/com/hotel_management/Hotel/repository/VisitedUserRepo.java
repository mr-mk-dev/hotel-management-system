package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.VisitedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public  interface VisitedUserRepo extends MongoRepository<VisitedUser,String> {
}


