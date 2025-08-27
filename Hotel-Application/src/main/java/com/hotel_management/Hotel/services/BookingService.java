package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @GetMapping("get")
    public String getVal(){
        return  "Hello brother ! Tum login ho chuke ho ";
    }


}
