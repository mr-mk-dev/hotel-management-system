package com.hotel_management.Hotel.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "foodOrders")
public class FoodOrder {
    @Id
    private String id;
    private String userId;
    private List<String> items;
    private double totalBill;
    private String status;
}

