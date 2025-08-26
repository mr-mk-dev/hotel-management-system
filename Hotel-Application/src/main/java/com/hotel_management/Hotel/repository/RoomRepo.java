package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public  interface RoomRepo extends MongoRepository<Room,String> {
}
