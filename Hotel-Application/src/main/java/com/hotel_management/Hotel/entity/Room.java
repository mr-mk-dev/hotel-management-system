package com.hotel_management.Hotel.entity;

import lombok.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document (collection = "rooms")
public class Room {

    @Id
    private String id;

    private String type;  // Single, Double, Suite

    private double pricePerNight;

    private String status;       // Available, Booked, Maintenance
}

