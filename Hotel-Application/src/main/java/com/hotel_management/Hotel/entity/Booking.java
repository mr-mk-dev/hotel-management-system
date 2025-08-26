package com.hotel_management.Hotel.entity;

import com.hotel_management.Hotel.enums.BookingStatus;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private User userId;
    private Room roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalAmount;
    private String paymentMode;   // Cash, UPI, Card
    private BookingStatus status;       // Confirmed, Active, Completed
}

