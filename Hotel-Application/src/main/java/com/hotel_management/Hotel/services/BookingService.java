package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.entity.Room;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.enums.BookingStatus;
import com.hotel_management.Hotel.repository.BookingRepo;
import com.hotel_management.Hotel.repository.RoomRepo;
import com.hotel_management.Hotel.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private UserRepo userRepo;

    private static final String BOOKBINDING = "No Booking Found";

    public ResponseEntity<?> addBooking(Booking booking) {
        String roomNo = booking.getRoomNo();
        Room room = roomRepo.findByRoomNo(roomNo).orElse(null);
        User user = userRepo.findById(booking.getUserId()).orElse(null);

        //Basic validation
        if (room == null) {
            return ResponseEntity.status(404).body("Room Number Not Exists");
        }
        if (user == null) {
            return ResponseEntity.status(404).body("User Not Exists , Try Register Now");
        }
        if (booking.getCheckIn() == null || booking.getCheckOut() == null) {
            return ResponseEntity.badRequest().body("Check-in and Check-out dates are required.");
        }
        if (!booking.getCheckOut().isAfter(booking.getCheckIn())) {
            return ResponseEntity.badRequest().body("Check-out date must be after check-in date.");
        }


        // Fetch existing bookings for this room
        List<Booking> existingBookings = bookingRepo.findByRoomNo(roomNo);

        // Filter out only relevant bookings
        existingBookings = existingBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED
                        || b.getStatus() == BookingStatus.PENDING
                        || b.getStatus() == BookingStatus.MAINTENANCE)
                .sorted(Comparator.comparing(Booking::getCheckIn))
                .toList();

        //Check conflicts
        for (Booking existing : existingBookings) {
            LocalDate existingIn = existing.getCheckIn();
            LocalDate existingOut = existing.getCheckOut();

            if (existingOut == null) {
                return ResponseEntity.status(409).body("Room " + roomNo + " is unavailable from "
                        + existingIn + " onwards (maintenance/blocked).");
            }

            boolean overlap = booking.getCheckIn().isBefore(existingOut)
                    && booking.getCheckOut().isAfter(existingIn);

            if (booking.getCheckIn().isEqual(existingOut)) {
                overlap = false;
            }

            if (overlap) {
                return ResponseEntity.status(409).body("Room " + roomNo + " is already booked from "
                        + existingIn + " to " + existingOut);
            }
        }

        //Save if no conflicts
        long daysStayed = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        double totalAmount = room.getPricePerNight() * daysStayed;
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);
        Booking saved = bookingRepo.save(booking);

        return ResponseEntity.ok(saved);
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
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByRoomNo(String roomNo) {
        List<Booking> bookings = bookingRepo.findByRoomNo(roomNo);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByRoomNoDateRange(String roomNo, LocalDate from, LocalDate to) {
        List<Booking> bookings =
                bookingRepo.findByRoomNoAndCheckInGreaterThanEqualAndCheckOutLessThanEqual(roomNo, from, to);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByDateRange(LocalDate from, LocalDate to) {
        List<Booking> bookings =
                bookingRepo.findByCheckInGreaterThanEqualAndCheckOutLessThanEqual(from, to);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByStatus(String status) {
        BookingStatus b = BookingStatus.valueOf(status.toUpperCase());
        List<Booking> bookings = bookingRepo.findByStatus(b);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByUserIdAndRoomNo(String userId, String roomNo) {
        List<Booking> bookings = bookingRepo.findAllByUserIdAndRoomNo(userId,roomNo);
        return ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByAmountBetween(double min, double max) {
        List<Booking> bookings = bookingRepo.findByTotalAmountBetween(min, max);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> findByCheckOut(LocalDate checkOut) {
        List<Booking> bookings = bookingRepo.findByCheckOut(checkOut);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> countByStatus(BookingStatus status) {
        long count = bookingRepo.countByStatus(status);
        return ResponseEntity.ok(count);
    }

    public ResponseEntity<?> findByPaymentMode(String paymentMode) {
        List<Booking> bookings = bookingRepo.findByPaymentMode(paymentMode);
        return bookings.isEmpty()
                ? ResponseEntity.status(204).body(BOOKBINDING)
                : ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> updateBooking(String id, Booking newBooking) {
        Room room = roomRepo.findByRoomNo(newBooking.getRoomNo()).orElse(null);
        Booking dbBooking = bookingRepo.findById(id).orElse(null);
        User u = userRepo.findById(newBooking.getUserId()).orElse(null);
        return bookingRepo.findById(id).map(existing -> {
            if (room == null || dbBooking == null || u == null) {
                return ResponseEntity.status(404).body("Data Mismatch (Either Room Not Found ," +
                        "User Not Found , Booking Not Found");
            }
            if (newBooking.getCheckIn() == null || newBooking.getCheckOut() == null) {
                return ResponseEntity.badRequest().body("Check-in and Check-out dates are required.");
            }
            if (!newBooking.getCheckOut().isAfter(newBooking.getCheckIn())) {
                return ResponseEntity.badRequest().body("Check-out date must be after check-in date.");
            }
            if (bookingRepo.existsByRoomNoAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                    newBooking.getRoomNo(), newBooking.getCheckIn(), newBooking.getCheckOut())) {
                return ResponseEntity.status(409).body("Booking conflict");
            }

            List<Booking> existingBookings = bookingRepo.findByRoomNo(newBooking.getRoomNo());

            // Filter out only relevant bookings
            existingBookings = existingBookings.stream()
                    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED
                            || b.getStatus() == BookingStatus.PENDING
                            || b.getStatus() == BookingStatus.MAINTENANCE)
                    .sorted(Comparator.comparing(Booking::getCheckIn))
                    .toList();

            //  Check conflicts
            for (Booking existing1 : existingBookings) {
                LocalDate existingIn = existing1.getCheckIn();
                LocalDate existingOut = existing1.getCheckOut();

                if (existingOut == null) {
                    return ResponseEntity.status(409).body("Room " + room.getRoomNo() + " is unavailable from "
                            + existingIn + " onwards (maintenance/blocked).");
                }

                boolean overlap = newBooking.getCheckIn().isBefore(existingOut)
                        && newBooking.getCheckOut().isAfter(existingIn);

                if (newBooking.getCheckIn().isEqual(existingOut)) {
                    overlap = false;
                }

                if (overlap) {
                    return ResponseEntity.status(409).body("Room " + room.getRoomNo() + " is already booked from "
                            + existingIn + " to " + existingOut);
                }
            }

            long daysStayed = ChronoUnit.DAYS.between(newBooking.getCheckIn(), newBooking.getCheckOut());
            double totalAmount = roomRepo.findByRoomNo(newBooking.getRoomNo()).get().getPricePerNight();
            existing.setCheckIn(newBooking.getCheckIn());
            existing.setCheckOut(newBooking.getCheckOut());
            existing.setStatus(BookingStatus.PENDING);
            existing.setTotalAmount(daysStayed * totalAmount);
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

    public ResponseEntity<?> confirmBooking(String id) {
        Booking booking = bookingRepo.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        if(booking.getStatus().equals(BookingStatus.PENDING)) {
            booking.setStatus(BookingStatus.CONFIRMED);
            bookingRepo.save(booking);
            return ResponseEntity.ok("Booking Confirmed");
        }
        return ResponseEntity.badRequest().body("Only Pending Bookings can be confirmed.");
    }
    public ResponseEntity<?> cancelBooking(String id) {
        Booking booking = bookingRepo.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        if(booking.getStatus().equals(BookingStatus.PENDING)) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepo.save(booking);
            return  ResponseEntity.ok("Booking Cancelled");
        }
        return ResponseEntity.badRequest().body("Only Pending Bookings can be cancelled.");
    }

    public ResponseEntity<?> completeBooking(String id) {
        Booking booking = bookingRepo.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }
        if(booking.getStatus().equals(BookingStatus.CONFIRMED)) {
            booking.setStatus(BookingStatus.COMPLETED);
            bookingRepo.save(booking);
            return ResponseEntity.ok("Booking has been completed");
        }
        return ResponseEntity.badRequest().body("Only Confirm Bookings can be completed.");
    }

}
