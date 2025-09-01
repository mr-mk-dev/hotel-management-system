package com.hotel_management.Hotel.enums;


public enum PaymentStatus {
    PENDING,    // Payment initiated but not yet completed
    SUCCESS,    // Payment completed successfully
    FAILED,     // Payment failed due to error/decline
    CANCELLED,  // User cancelled the payment flow
    REFUNDED    // Money refunded to user after cancellation
}

