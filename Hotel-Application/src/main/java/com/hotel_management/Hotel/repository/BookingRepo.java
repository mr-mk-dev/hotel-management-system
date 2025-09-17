package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.enums.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepo extends MongoRepository<Booking, String> {

    // 1. Find bookings by room number
    List<Booking> findByRoomNo(String roomNo);

    // 2. Find bookings by roomNo within a date range
    List<Booking> findByRoomNoAndCheckInGreaterThanEqualAndCheckOutLessThanEqual(
            String roomNo, LocalDate from, LocalDate to);

    // 3. Find bookings by date range
    List<Booking> findByCheckInGreaterThanEqualAndCheckOutLessThanEqual(LocalDate from, LocalDate to);

    // 4. Find bookings by status
    List<Booking> findByStatus(BookingStatus status);

    // 5. Find bookings by customer
    List<Booking> findByUserId(String userId);

    // 6. Find booking by customer + room
    List<Booking> findAllByUserIdAndRoomNo(String userId, String roomNo);

    // 7. Check active bookings for a room (to avoid double-booking conflicts)
    List<Booking> findByRoomNoAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
            String roomNo, LocalDate date1, LocalDate date2);

    // 8. Find by payment mode (Cash, UPI, Card…)
    List<Booking> findByPaymentMode(String paymentMode);

    // 9. Count bookings for analytics
    long countByStatus(BookingStatus status);

    // 10. Check if a room is already booked between given dates
    boolean existsByRoomNoAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
            String roomNo, LocalDate checkOut, LocalDate checkIn);

    // 11. Find bookings within price range
    List<Booking> findByTotalAmountBetween(double min, double max);

    // 12. Find bookings by checkout date range
    List<Booking> findByCheckOutBetween(LocalDate start, LocalDate end);

    // 13. Find latest booking of a user (to check last visit)
    Optional<Booking> findTopByUserIdOrderByCheckOutDesc(String userId);

    // 14. Find upcoming bookings for a user (future check-ins)
    List<Booking> findByUserIdAndCheckInAfter(String userId, LocalDate today);

    // 15. Cancelled/Completed bookings for reports
    List<Booking> findByStatusIn(List<BookingStatus> statuses);

    // 16. Find all active bookings on a specific date (for hotel occupancy)
    List<Booking> findByCheckInLessThanEqualAndCheckOutGreaterThanEqual(LocalDate date1, LocalDate date2);

    List<Booking> findByCheckOut(LocalDate date);
}
