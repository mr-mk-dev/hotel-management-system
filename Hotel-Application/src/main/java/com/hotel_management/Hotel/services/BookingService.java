package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.enums.BookingStatus;
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


    public ResponseEntity<?> addBooking(Booking booking) {
        if (booking.getCheckOut().isBefore(booking.getCheckIn())) {
            return ResponseEntity.badRequest().body("Check-out cannot be before check-in");
        }
        boolean exists = bookingRepo.existsByRoomNoAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                booking.getRoomNo(), booking.getCheckOut(), booking.getCheckIn());
        if (exists) {
            return ResponseEntity.status(409).body("Conflict: Room already booked");
        }
        return ResponseEntity.status(201).body(bookingRepo.save(booking));
    }

    public ResponseEntity<?> allBookingList() {
        List<Booking> bookings = bookingRepo.findAll();
        return bookings.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<Booking> findById(String id) {
        return bookingRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    public ResponseEntity<?> findByUserId(String userId) {
        List<Booking> bookings = bookingRepo.findByUserId(userId);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByRoomNo(String roomNo) {
        List<Booking> bookings= bookingRepo.findByRoomNo(roomNo);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByRoomNoDateRange(String roomNo, LocalDate from, LocalDate to) {
        List<Booking> bookings = bookingRepo
                .findByRoomNoAndCheckInGreaterThanEqualAndCheckOutLessThanEqual(roomNo, from, to);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByDateRange(LocalDate from, LocalDate to) {
        List<Booking> bookings = bookingRepo
                .findByCheckInGreaterThanEqualAndCheckOutLessThanEqual(from, to);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepo.findByStatus(status);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByUserIdAndRoomNo(String userId, String roomNo) {
        List<Booking> bookings = bookingRepo.findByUserIdAndRoomNo(userId, roomNo);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByAmountBetween(double min, double max){
        List<Booking> bookings = bookingRepo.findByTotalAmountBetween(min,max);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByCheckOut(LocalDate checkOut){
        List<Booking> bookings = bookingRepo.findByCheckOut(checkOut);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> countByStatus(BookingStatus status){
       long count =  bookingRepo.countByStatus(status);
       return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> findByPaymentMode(String paymentMode){
        List<Booking> bookings  = bookingRepo.findByPaymentMode(paymentMode);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body("No bookings found")
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> updateBooking(String id, Booking newBooking) {
        return bookingRepo.findById(id)
                .map(existing -> {
                    existing.setCheckIn(newBooking.getCheckIn());
                    existing.setCheckOut(newBooking.getCheckOut());
                    existing.setStatus(newBooking.getStatus());
                    existing.setTotalAmount(newBooking.getTotalAmount());
                    existing.setPaymentMode(newBooking.getPaymentMode());
                    existing.setRoomNo(newBooking.getRoomNo());
                    existing.setUserId(newBooking.getUserId());
                    return ResponseEntity.ok(bookingRepo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteBooking(String id) {
        return bookingRepo.findById(id)
                .map(existing -> {
                    bookingRepo.delete(existing);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }



}
