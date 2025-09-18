package com.hotel_management.Hotel.controller;


import com.hotel_management.Hotel.services.email.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService  mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {

        mailService.sendMail(to, subject, body);
        return ResponseEntity.ok("Mail sent successfully to " + to);
    }
}

