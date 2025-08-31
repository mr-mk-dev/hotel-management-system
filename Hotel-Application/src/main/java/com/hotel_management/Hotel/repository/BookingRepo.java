package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.enums.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface BookingRepo extends MongoRepository<Booking, String> {

    // 1. Find bookings by room number
    List<Booking> findByRoomNo(String roomNo);

    // 2. Find bookings by roomNo and date range
    List<Booking> findByRoomNoAndCheckInGreaterThanEqualAndCheckOutLessThanEqual(
            String roomNo, java.util.Date from, java.util.Date to);

    // 3. Find bookings by date range
    List<Booking> findByCheckInGreaterThanEqualAndCheckOutLessThanEqual(
            Date from, Date to);

    // 4. Find bookings by booking status
    List<Booking> findByStatus(BookingStatus status);

    // 5. Find bookings by customerId
    List<Booking> findByUserId(String userId);

    // 6. Find bookings by customerId + roomNo
    List<Booking> findByUserIdAndRoomNo(String userId, String roomNo);

    // 7. Find active bookings for a room at a given date (very important for conflicts)
    List<Booking> findByRoomNoAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
            String roomNo, Date date1, Date date2);

    // 8. Find bookings by payment mode
    List<Booking> findByPaymentMode(String paymentMode);

    // 9. Count total bookings for analytics
    long countByStatus(BookingStatus status);

    // 10. Check if a room is already booked between given dates
    boolean existsByRoomNoAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
            String roomNo, Date checkOut, Date checkIn);

    // 11. Find list of room between range of room of bookings
    List<Booking> findByTotalAmountBetween(double min, double max);

//    12 . We can find list of booking by checkout date
    List<Booking> findByCheckOut(Date checkOut);

}
