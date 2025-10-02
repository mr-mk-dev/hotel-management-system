package com.hotel_management.Hotel.entity.Food;

import lombok.Data;

@Data
public class OrderedFood {
    private String foodId;  // Reference to Food
    private String name;    // Food name (stored at the time of order)
    private double price;   // Price at the time of order
    private int quantity;   // Quantity ordered
}

//OrderedFood → embedded inside FoodOrder to keep a snapshot of the order.