package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<?> addRoom(@RequestBody Booking booking){
       return bookingService.addBooking(booking);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Booking booking){
        return bookingService.updateBooking(id,booking);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        return bookingService.deleteBooking(id);
    }

    @GetMapping("/list")
    public ResponseEntity<?> listAll(){
        return bookingService.allBookingList();
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        return bookingService.findById(id);
    }

    @GetMapping("/find-by-user-id/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable String userId ){
        System.out.println(bookingService.findByUserId(userId));
        return bookingService.findByUserId(userId);
    }

    @GetMapping("/find-by-room-no/{roomNo}")
    public ResponseEntity<?> findByRoomNo(@PathVariable String roomNo ){
        return bookingService.findByRoomNo(roomNo);
    }

    @GetMapping("/find-by-room-range")
    public ResponseEntity<?> findByRoomDateRange(
            @RequestParam String roomNo,
            @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date from,
            @RequestParam @DateTimeFormat(pattern="dd-MM-yyyy") Date to){
        return bookingService.findByRoomNoDateRange(roomNo,from,to);
    }

    @GetMapping("/find-by-date-range")
    public ResponseEntity<?> findByDateRange(
            @RequestParam Date from ,
            @RequestParam Date to){
        return bookingService.findByDateRange(from,to);
    }




}
