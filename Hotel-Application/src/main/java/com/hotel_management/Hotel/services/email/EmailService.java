//package com.hotel_management.Hotel.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender javaMailSender;
//
//    public void sendMail(String to, String subject, String body) {
//        try {
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//            mailMessage.setFrom("manish825316@gmail.com");
//            mailMessage.setTo(to);
//            mailMessage.setText(body);
//            mailMessage.setSubject(subject);
//            javaMailSender.send(mailMessage);
//            System.out.println("Mail send successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
