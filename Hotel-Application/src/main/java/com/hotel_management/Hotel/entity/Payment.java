package com.hotel_management.Hotel.entity;

import com.hotel_management.Hotel.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    private String id;

    private Booking bookingId;         // reference to Booking
    private User userId;           // reference to Guest
    private double amount;            // total amount paid
    private String paymentMethod;     // CARD, UPI, CASH, NET_BANKING
    private PaymentStatus status;     // PENDING, SUCCESS, FAILED
    private LocalDateTime timestamp;  // when payment was made
}
