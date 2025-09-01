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

    private String bookingId;
    private String userId;

    // Payment details
    private double amount;
    private String currency;           // INR, USD etc. (Razorpay needs this)
    private String paymentMethod;      // CARD, UPI, CASH, NET_BANKING

    // Status
    private PaymentStatus status;      // PENDING, SUCCESS, FAILED, REFUNDED
    private LocalDateTime timestamp;   // when payment was made

    // for Razorpay/Stripe/etc
//    payment.setProvider("MOCK");
//    payment.setProviderTransactionId(UUID.randomUUID().toString());
//    payment.setProviderResponse("Simulated Payment Success");

    private String provider;               // MOCK, RAZORPAY, STRIPE
    private String providerTransactionId;  // transaction from provider
    private String providerResponse;       // full JSON/raw response for audit
}
