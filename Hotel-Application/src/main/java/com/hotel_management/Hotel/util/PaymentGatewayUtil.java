//package com.hotel_management.Hotel.util;
//
//import com.hotel_management.Hotel.config.RazorpayConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//
//@Component
//@RequiredArgsConstructor
//public class PaymentGatewayUtil {
//
//    @Autowired
//    private RazorpayConfig payConfig;
//
//    public boolean verifyRazorpaySignature(String orderId, String paymentId, String signature) {
//        try {
//            String payload = orderId + '|' + paymentId;
//            Mac mac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(payConfig.getKeySecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//            mac.init(secretKeySpec);
//            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
//            String expected = bytesToHex(digest);
//            return expected.equals(signature);
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    private static String bytesToHex(byte[] bytes) {
//        StringBuilder sb = new StringBuilder(bytes.length * 2);
//        for (byte b : bytes) sb.append(String.format("%02x", b));
//        return sb.toString();
//    }
//}
