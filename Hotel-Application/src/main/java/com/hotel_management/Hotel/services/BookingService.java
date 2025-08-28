package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private RoomService roomService;

    public ResponseEntity<?> allBookingList(){
        return ResponseEntity.ok(bookingRepo.findAll());
    }

    public ResponseEntity<?> findById(String id){
        Booking booking = bookingRepo.findById(id).orElse(null);
        if(booking !=null){
            return ResponseEntity.ok(booking);
        }
        return ResponseEntity.status(404).body(null);
    }

    public ResponseEntity<?> findByUserId(String userId){
        List<Booking> booking = bookingRepo.findByUserId(userId);
            return ResponseEntity.ok(booking);
    }

    public ResponseEntity<?> findByRoomNo(String roomNo){
        List<Booking> booking = bookingRepo.findByRoomNo(roomNo);
            return ResponseEntity.ok(booking);
    }

    public ResponseEntity<?> findByRoomNoDateRange(String roomNo, LocalDate from, LocalDate to ){
        List<Booking> bookings = bookingRepo
                .findByRoomNoAndCheckInGreaterThanEqualAndCheckOutLessThanEqual(roomNo,from,to);
        return ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByDateRange(LocalDate from, LocalDate to ){
        List<Booking> bookings = bookingRepo
                .findByCheckInGreaterThanEqualAndCheckOutLessThanEqual(from,to);
        return ResponseEntity.ok(bookings);
    }

    






}
