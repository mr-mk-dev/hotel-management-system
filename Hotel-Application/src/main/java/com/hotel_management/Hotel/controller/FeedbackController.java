package com.hotel_management.Hotel.controller;

import com.hotel_management.Hotel.entity.Booking;
import com.hotel_management.Hotel.entity.Feedback;
import com.hotel_management.Hotel.entity.User;
import com.hotel_management.Hotel.enums.BookingStatus;
import com.hotel_management.Hotel.enums.Role;
import com.hotel_management.Hotel.repository.BookingRepo;
import com.hotel_management.Hotel.repository.UserRepo;
import com.hotel_management.Hotel.services.Custom.CustomUserDetails;
import com.hotel_management.Hotel.services.FeedbackService;
import com.mongodb.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final BookingRepo bookingRepo;
    private final UserRepo userRepo;

    public FeedbackController(FeedbackService feedbackService, BookingRepo bookingRepo, UserRepo userRepo) {
        this.feedbackService = feedbackService;
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/add/{bookingId}")
    public ResponseEntity<?> addFeedback(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String bookingId,
            @RequestBody Feedback feedback
    ){
        String emailId = userDetails.getUsername();
        User user = userRepo.findByEmail(emailId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if((Role.USER!=user.getRole())){
            return ResponseEntity.badRequest().body("You are not allowed to perform this operation");
        }
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (!user.getId().equals(booking.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are only allowed to post feedback.");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Feedback is allowed only after checkout (booking must be COMPLETED).");
        }

        if (!feedbackService.findByBookingId(bookingId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Feedback already exists for this booking");
        }
        feedback.setRating(feedback.getRating());
        feedback.setComment(feedback.getComment());
        feedback.setCreatedAt(LocalDateTime.now());

        try {
            Feedback saved = feedbackService.saveFeedback(feedback);
            return ResponseEntity.ok(saved);
        } catch (DuplicateKeyException ex) {
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
        String emailId = userDetails.getUsername();
        User user = userRepo.findByEmail(emailId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if((Role.USER!=user.getRole())){
            return ResponseEntity.badRequest().body("You are not allowed to perform this operation");
        }

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

    @GetMapping("/list")
    public ResponseEntity<?> getFeedbacks() {
        return ResponseEntity.ok(feedbackService.findAll());
    }

}

