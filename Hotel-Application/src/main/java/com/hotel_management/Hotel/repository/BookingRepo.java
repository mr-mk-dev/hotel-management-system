package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface BookingRepo extends MongoRepository<Booking,String> {

}