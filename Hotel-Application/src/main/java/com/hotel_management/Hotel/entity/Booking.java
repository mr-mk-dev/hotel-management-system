package com.hotel_management.Hotel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotel_management.Hotel.enums.BookingStatus;
import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String userId;
    private String roomNo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date checkIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date checkOut;
    private double totalAmount;
    private String paymentMode;   // Cash, UPI, Card
    private BookingStatus status;       // Confirmed, Active, Completed
}

