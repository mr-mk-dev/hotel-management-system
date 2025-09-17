package com.hotel_management.Hotel.entity;

import com.hotel_management.Hotel.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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

    @Indexed(unique = true)
    private String bookingId;

    private String userId;

    // Payment details
    private double amount;
    private String currency;
    private String paymentMethod;

    // Status
    private PaymentStatus status;
    private LocalDateTime timestamp;

    // Payment gateway info
    private String provider;
    private String providerTransactionId;
    private String providerResponse;
}
