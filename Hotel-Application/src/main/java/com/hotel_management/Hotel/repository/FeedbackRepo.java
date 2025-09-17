package com.hotel_management.Hotel.repository;

import com.hotel_management.Hotel.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeedbackRepo extends MongoRepository<Feedback,String> {
    List<Feedback> findByBookingId(String bookingId);
    List<Feedback> findByRatingIsGreaterThanEqual(int rating);
    List<Feedback> findByRatingIsLessThanEqual(int rating);
    List<Feedback> findByRating(int rating);
}
