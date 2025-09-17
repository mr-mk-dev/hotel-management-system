package com.hotel_management.Hotel.services;

import com.hotel_management.Hotel.entity.Feedback;
import com.hotel_management.Hotel.repository.FeedbackRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

   private final FeedbackRepo  feedbackRepo;
   public FeedbackService(FeedbackRepo feedbackRepo) {
       this.feedbackRepo = feedbackRepo;
   }

   public Feedback getFeedbackById(String id) {
        return feedbackRepo.findById(id).orElse(null);
   }

   public Feedback saveFeedback(Feedback feedback) {
       return feedbackRepo.save(feedback);
   }
   public Feedback updateFeedback(String id , Feedback feedback) {
       Feedback feedbackRepo1 = feedbackRepo.findById(id).orElse(null);
       if(feedbackRepo1 != null){
           feedbackRepo1.setRating(feedback.getRating());
           feedbackRepo1.setComment(feedback.getComment());
           return feedbackRepo.save(feedbackRepo1);
       }
       return null;
   }
   public List<Feedback> feedbackList() {
       return  feedbackRepo.findAll();
   }

   public List<Feedback> findByBookingId(String bookingId) {
        return feedbackRepo.findByBookingId(bookingId);
   }

   public List<Feedback>  findByRatingIsGreaterThanEqual(int rating) {
       return feedbackRepo.findByRatingIsGreaterThanEqual(rating);
   }
   public  List<Feedback> findByRating(int rating) {
        return feedbackRepo.findByRating(rating);
   }
   public List<Feedback> findByRatingIsLessThanEqual(int rating) {
       return feedbackRepo.findByRatingIsLessThanEqual(rating);
   }
}
