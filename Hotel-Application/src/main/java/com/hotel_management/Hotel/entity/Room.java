package com.hotel_management.Hotel.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "rooms")
public class Room {

    private Boolean success;
    private String message;

    @Id
    private String id;

    @Indexed(unique = true)
    private String roomNo;

    private String type;

    private double pricePerNight;

    private List<ImageResource> images = new ArrayList<>();

}


