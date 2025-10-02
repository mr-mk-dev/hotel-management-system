package com.hotel_management.Hotel.services.payment;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;

    public PaymentService(
        @Value("${razorpay.key.id}") String keyId,
        @Value("${razorpay.key.secret}") String keySecret
    ) throws Exception {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    public Order createOrder(double amount, String currency, String receipt) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(amount * 100)); // Amount in paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);
        return razorpayClient.orders.create(orderRequest);
    }
}