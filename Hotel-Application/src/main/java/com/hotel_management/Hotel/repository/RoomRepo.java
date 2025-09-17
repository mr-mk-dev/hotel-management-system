package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public  interface RoomRepo extends MongoRepository<Room,String> {

    Optional<Room> findByRoomNo(String roomNo);

    Optional<List<Room>> findByType(String type);

    // Find rooms with price greater than given value
    Optional<List<Room>> findByPricePerNightGreaterThan(double price);

    // Find rooms with price less than given value
    Optional<List<Room>> findByPricePerNightLessThan(double price);

    // Find rooms with price between min and max
   Optional<List<Room>> findByPricePerNightBetween(double minPrice,
                                                   double maxPrice);
   Optional<Room> deleteByRoomNo(String roomNo);

   boolean existsByRoomNo(String roomNo);

}
