package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.entity.Feedback;
import com.hotel_management.Hotel.enums.BookingStatus;
import com.hotel_management.Hotel.repository.BookingRepo;
import com.hotel_management.Hotel.services.Custom.CustomUserDetails;
import com.hotel_management.Hotel.services.FeedbackService;
import com.mongodb.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final BookingRepo bookingRepo;

    public FeedbackController(FeedbackService feedbackService, BookingRepo bookingRepo) {
        this.feedbackService = feedbackService;
        this.bookingRepo = bookingRepo;
    }

    @PostMapping("/add/{bookingId}")
    public ResponseEntity<?> addFeedback(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String bookingId,
            @RequestBody Feedback feedback
    ) {
        String userId = userDetails.getUsername();

        // 1) Check booking exists first
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        // 2) Authorization: only booking owner may submit feedback
        if (!userId.equals(booking.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are only allowed to post feedback for your own booking.");
        }

        // 3) Optional: allow feedback only after checkout/completion
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Feedback is allowed only after checkout (booking must be COMPLETED).");
        }

        // 4) Prevent duplicate feedback (race safe approach: rely on unique index + handle duplicate key)
        if (!feedbackService.findByBookingId(bookingId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Feedback already exists for this booking");
        }

        // 5) Enforce server-side ownership of feedback data
        feedback.setRating(feedback.getRating());
        feedback.setComment(feedback.getComment());
        feedback.setCreatedAt(LocalDateTime.now());

        try {
            Feedback saved = feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok(saved);
        } catch (DuplicateKeyException ex) {
            // guard against a race condition creating two feedbacks concurrently
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Feedback already exists (race detected).");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save feedback");
        }
    }

    @PutMapping("/update/{feedbackId}")
    public ResponseEntity<?> updateFeedback(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String feedbackId,
            @RequestBody Feedback feedback) {

        String userId = userDetails.getUsername();

        Feedback existing = feedbackService.getFeedbackById(feedbackId);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found");
        }

        Booking booking = bookingRepo.findById(existing.getBookingId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (!booking.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own feedback");
        }

        existing.setRating(feedback.getRating());
        existing.setComment(feedback.getComment());

        return ResponseEntity.ok(feedbackService.saveFeedback(existing));
    }


}

