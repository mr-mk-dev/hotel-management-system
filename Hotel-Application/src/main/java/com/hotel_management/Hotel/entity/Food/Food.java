package com.hotel_management.Hotel.entity.Food;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "food_items")
public class Food {
    @Id
    private String id;         // Unique ID
    private String name;       // Name of the food item
    private String category;   // e.g., "Starters", "Main Course"
    private double price;      // Price per unit
}

//Food → stores menu items.

