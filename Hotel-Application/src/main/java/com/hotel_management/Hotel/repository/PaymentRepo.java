package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepo extends MongoRepository<Payment,String> {
}