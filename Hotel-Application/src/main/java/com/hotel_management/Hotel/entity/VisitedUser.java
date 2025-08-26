package com.hotel_management.Hotel.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "visited_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitedUser {

    @Id
    private String id;

    private User userId;                // reference to original Guest
    private User name;
    private User email;
    private User phone;

    private List<String> bookingIds;       // all bookings done by guest
    private List<String> paymentIds;       // all payments made by guest

    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private String feedback;               // optional feedback from guest
}


