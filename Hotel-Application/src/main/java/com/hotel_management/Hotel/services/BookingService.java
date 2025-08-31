package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.enums.BookingStatus;
import com.hotel_management.Hotel.repository.BookingRepo;
import com.hotel_management.Hotel.repository.RoomRepo;
import com.hotel_management.Hotel.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private UserRepo userRepo;

    public ResponseEntity<?> addBooking(Booking booking) {
        // 1. Basic validation
        if (roomRepo.findByRoomNo(booking.getRoomNo()).isEmpty()) {
            return ResponseEntity.status(404).body("Room Number Not Exists");
        }
        User u = userRepo.findById(booking.getUserId()).orElse(null);
        System.out.println(u);
        if (u == null) {
            return ResponseEntity.status(404).body("User Not Exists , Try Register Now");
        }
        if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
            return ResponseEntity.badRequest().body("Check-in and Check-out dates are required.");
        }
        if (!booking.getCheckOut().after(booking.getCheckIn())) {
            return ResponseEntity.badRequest().body("Check-out date must be after check-in date.");
        }

        String roomNo = booking.getRoomNo();

        // 2. Fetch existing bookings for this room
        List<Booking> existingBookings = bookingRepo.findByRoomNo(roomNo);

        // Filter out only relevant bookings (Confirmed, Pending, Maintenance block the room)
        existingBookings = existingBookings.stream().filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING || b.getStatus() == BookingStatus.MAINTENANCE).sorted(Comparator.comparing(Booking::getCheckIn)).toList();

        // 3. Check conflicts
        for (Booking existing : existingBookings) {
            Date existingIn = existing.getCheckIn();
            Date existingOut = existing.getCheckOut();

            // Case: open-ended booking (maintenance forever)
            if (existingOut == null) {
                return ResponseEntity.status(409).body("Room " + roomNo + " is unavailable from " + existingIn + " onwards (maintenance/blocked).");
            }

            // Case: overlapping booking
            boolean overlap = booking.getCheckIn().before(existingOut) && // starts before other ends
                    booking.getCheckOut().after(existingIn);   // ends after other starts

            // Allow same-day checkout->checkin (e.g., existingOut == booking.checkIn)
            if (booking.getCheckIn().equals(existingOut)) {
                overlap = false;
            }

            if (overlap) {
                return ResponseEntity.status(409).body("Room " + roomNo + " is already booked from " + existingIn + " to " + existingOut);
            }
        }

        // 4. Save if no conflicts
        booking.setStatus(BookingStatus.CONFIRMED); // or default
        Booking saved = bookingRepo.save(booking);

        return ResponseEntity.ok(saved);
    }

    public ResponseEntity<?> allBookingList() {
        List<Booking> bookings = bookingRepo.findAll();
        return bookings.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<Booking> findById(String id) {
        return bookingRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> findByUserId(String userId) {
        List<Booking> bookings = bookingRepo.findByUserId(userId);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByRoomNo(String roomNo) {
        List<Booking> bookings = bookingRepo.findByRoomNo(roomNo);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByRoomNoDateRange(String roomNo, Date from, Date to) {
        List<Booking> bookings = bookingRepo.findByRoomNoAndCheckInGreaterThanEqualAndCheckOutLessThanEqual(roomNo, from, to);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByDateRange(Date from, Date to) {
        List<Booking> bookings = bookingRepo.findByCheckInGreaterThanEqualAndCheckOutLessThanEqual(from, to);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByStatus(BookingStatus status) {
        List<Booking> bookings = bookingRepo.findByStatus(status);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByUserIdAndRoomNo(String userId, String roomNo) {
        List<Booking> bookings = bookingRepo.findByUserIdAndRoomNo(userId, roomNo);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByAmountBetween(double min, double max) {
        List<Booking> bookings = bookingRepo.findByTotalAmountBetween(min, max);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByCheckOut(Date checkOut) {
        List<Booking> bookings = bookingRepo.findByCheckOut(checkOut);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> countByStatus(BookingStatus status) {
        long count = bookingRepo.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> findByPaymentMode(String paymentMode) {
        List<Booking> bookings = bookingRepo.findByPaymentMode(paymentMode);
        return bookings.isEmpty() ? ResponseEntity.status(204).body("No bookings found") : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> updateBooking(String id, Booking newBooking) {
        return bookingRepo.findById(id).map(existing -> {
            existing.setCheckIn(newBooking.getCheckIn());
            existing.setCheckOut(newBooking.getCheckOut());
            existing.setStatus(newBooking.getStatus());
            existing.setTotalAmount(newBooking.getTotalAmount());
            existing.setPaymentMode(newBooking.getPaymentMode());
            existing.setRoomNo(newBooking.getRoomNo());
            existing.setUserId(newBooking.getUserId());
            return ResponseEntity.ok(bookingRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteBooking(String id) {
        return bookingRepo.findById(id).map(existing -> {
            bookingRepo.delete(existing);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }


}
