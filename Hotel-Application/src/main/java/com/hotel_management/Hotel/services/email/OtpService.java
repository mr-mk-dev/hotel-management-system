package com.hotel_management.Hotel.services.email;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> otpExpiry = new ConcurrentHashMap<>();
    private final MailService mailService;
    private final int OTP_VALID_DURATION = 5 * 60 * 1000;

    public OtpService(MailService mailService) {
        this.mailService = mailService;
    }

    public String generateOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        otpStorage.put(email, otp);
        otpExpiry.put(email, System.currentTimeMillis() + OTP_VALID_DURATION);

        mailService.sendMail(email, "OTP Verification", "Your OTP is: " + otp);
        return "OTP sent to " + email;
    }

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;

        String storedOtp = otpStorage.get(email);
        Long expiryTime = otpExpiry.get(email);

        if (expiryTime < System.currentTimeMillis()) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }

        boolean valid = storedOtp.equals(otp);

        if (valid) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
        }

        return valid;
    }
}
