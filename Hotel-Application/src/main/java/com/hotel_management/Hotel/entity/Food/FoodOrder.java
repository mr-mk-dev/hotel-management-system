package com.hotel_management.Hotel.entity.Food;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "food_orders")
public class FoodOrder {
    @Id
    private String id;           // Unique order ID
    private String bookingId;    // Link to a valid booking
    private List<OrderedFood> items; // List of ordered food items
    private double totalAmount;  // Total cost of this order
    private String status;       // e.g., "PENDING", "COMPLETED"
}

//FoodOrder → stores orders linked to a booking.
