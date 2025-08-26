package com.hotel_management.Hotel.dto;

import lombok.*;
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}

