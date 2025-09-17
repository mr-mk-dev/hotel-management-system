package com.hotel_management.Hotel.entity;

import lombok.Data;

@Data
public class ImageResource {
    private String url;       // for displaying
    private String publicId;  // for deleting
}
