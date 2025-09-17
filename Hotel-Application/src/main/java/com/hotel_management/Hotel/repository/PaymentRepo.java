package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.hotel_management.Hotel.enums.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends MongoRepository<Payment, String> {

    // Find payments by user
    List<Payment> findByUserId(String userId);

    Optional<Payment> findByBookingId(String bookingId);

    // Find payments by status
    List<Payment> findByStatus(PaymentStatus status);

    // Find latest payment for a booking
    Optional<Payment> findTopByUserIdOrderByTimestampDesc(String bookingId);
}
