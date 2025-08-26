package com.hotel_management.Hotel.entity;


import com.hotel_management.Hotel.enums.FoodCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItem{

    @Id
    private String id;

    private String name;            // e.g. "Paneer Butter Masala"
    private String description;     // short details of dish
    private double price;           // price per item
    private FoodCategory category;  // VEG, NON_VEG, DRINKS, DESERTS, etc.
    private boolean available;      // is item currently available?
}

