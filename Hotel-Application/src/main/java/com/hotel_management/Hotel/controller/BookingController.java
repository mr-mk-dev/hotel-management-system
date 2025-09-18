package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.enums.BookingStatus;
import com.hotel_management.Hotel.repository.BookingRepo;
import com.hotel_management.Hotel.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate to){
        return bookingService.findByRoomNoDateRange(roomNo,from,to);
    }

    @GetMapping("/find-by-date-range")
    public ResponseEntity<?> findByDateRange(
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate to) {
        return bookingService.findByDateRange(from,to);
    }

    @GetMapping("find-by-payment-method/{paymentMethod}")
    public ResponseEntity<?> findByPaymentMethod(@PathVariable String paymentMethod){
        return bookingService.findByPaymentMode(paymentMethod.toUpperCase());
    }

    @GetMapping("count-by-status/{status}")
    public ResponseEntity<?> countByStatus(@PathVariable String status){
        return  bookingService.countByStatus(BookingStatus.valueOf(status.toUpperCase()));
    }

    @GetMapping("find-by-checkout/{checkOut}")
    public ResponseEntity<?> findByCheckoutDate(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate checkOut) {
        return bookingService.findByCheckOut(checkOut);
    }

    @GetMapping("find-by-status/{status}")
    public ResponseEntity<?> findByStatus(@PathVariable  String status){
        return bookingService.findByStatus(status);
    }

    @GetMapping("find-by-user-id-room-no/{userId}/{roomNo}")
    public ResponseEntity<?> findByUserIdRoomNo(@PathVariable String userId,@PathVariable String roomNo){
        return bookingService.findByUserIdAndRoomNo(userId,roomNo);
    }

    @GetMapping("find-by-amount-between/{from}/{to}")
    public ResponseEntity<?> findByAmountBetween(@PathVariable double from,@PathVariable double to){
        return bookingService.findByAmountBetween(from,to);
    }

    @PutMapping("complete/{bookingId}")
    public ResponseEntity<?>  completeBooking(@PathVariable String bookingId){
       return bookingService.completeBooking(bookingId);
    }
    @PutMapping("confirm/{bookingId}")
    public ResponseEntity<?>  confirmBooking(@PathVariable String bookingId){
        return bookingService.confirmBooking(bookingId);
    }
    @PutMapping("cancel/{bookingId}")
    public ResponseEntity<?>  cancelBooking(@PathVariable String bookingId){
        return bookingService.cancelBooking(bookingId);
    }

}
