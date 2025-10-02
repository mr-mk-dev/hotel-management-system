package com.hotel_management.Hotel.services.email;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final JavaMailSender mailSender;

    // OTP storage in memory
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, String> otpStorageLogin= new ConcurrentHashMap<>();
    private final Map<String, Long> otpExpiry = new ConcurrentHashMap<>();

    private static final int OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes

    public OtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String generateOtp(String email,String name) {
        String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        otpStorage.put(email, otp);

        String subject = "OTP for Account Verification – Hotel Management System";

        String template = """
        Dear [User’s Name],
        
        Your One-Time Password (OTP) for completing the registration process is:
        
        -->                          [OTP Code]                             <--
        
        This OTP is valid for 5 minutes only. Please enter it promptly to verify your account.
        
        ⚠️ For your security, do not disclose this OTP to anyone. If you did not initiate this request, kindly disregard this email.
        
        Sincerely,
        Hotel Management Administration
        """;

        String body = template.replace("[User’s Name]", name)
                .replace("[OTP Code]", otp);


        sendMail(email,subject, body);
        return "OTP sent to " + email;
    }

    public String generateOtpLogin(String email,String name) {
        String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));
        otpStorageLogin.put(email, otp);

        String subject = "Secure Your Login with This OTP – Hotel Management System";

        String template = """
                Dear [User’s Name],
                
                We noticed a login attempt on your account. To keep your account safe, we’ve generated a one-time code for you:
                
                ✨  [OTP Code]  ✨
                
                This OTP will expire in 5 minutes.
                Please use it to finish logging in and enjoy our services securely.
                
                If this wasn’t you, don’t worry — your account is safe. Simply ignore this email.
                
                Warm regards,
                **Hotel Management Team**
                
        """;

        String body = template.replace("[User’s Name]", name)
                .replace("[OTP Code]", otp);


        sendMail(email,subject, body);
        return "OTP sent to " + email;
    }

    private void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("manish825316@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try{
            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;

        boolean isValid = otpStorage.get(email).equals(otp) ;
//        &&
//        System.currentTimeMillis() < otpExpiry.get(email);

        if (isValid) {
            otpStorage.remove(email);
//            otpExpiry.remove(email);
        }
        return isValid;
    }

    public boolean validateOtpLogin(String email, String otp) {
        if (!otpStorageLogin.containsKey(email))
            return false;

        String storedOtp = otpStorageLogin.get(email);
        boolean isValid = otp.equals(storedOtp);

        if (isValid) {
            otpStorageLogin.remove(email);
        }

        return isValid;
    }

}

