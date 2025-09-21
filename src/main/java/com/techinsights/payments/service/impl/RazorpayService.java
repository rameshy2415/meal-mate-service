package com.techinsights.payments.service.impl;


import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RazorpayService implements IRazorpayService {

    @Value("${razorpay.key}")
    private String keyId;

    @Value("${razorpay.secret}")
    private String keySecret;

    @Override
    public Order createOrder(BigDecimal amount) throws Exception {
        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", convertRupeesToPaisa(amount)); // amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt_" + System.currentTimeMillis());
        var razorResponse = razorpay.orders.create(orderRequest);
        System.out.println(razorResponse);
        return razorResponse;
    }

    @Override
    public boolean verifyPayment(String orderId, String paymentId, String signature) throws Exception {
        String data = orderId + "|" + paymentId;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);

        byte[] hash = sha256_HMAC.doFinal(data.getBytes());

        // Convert to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        String generatedSignature = hexString.toString();

        return generatedSignature.equals(signature);
    }


    public static long convertRupeesToPaisa(BigDecimal amountInRupees) {
        // Define the multiplier for converting rupees to paisa
        BigDecimal multiplier = new BigDecimal("100");

        // Multiply the amount by 100 to get paisa
        // Use RoundingMode.HALF_UP for standard rounding
        BigDecimal amountInPaisaBigDecimal = amountInRupees.multiply(multiplier).setScale(0, RoundingMode.HALF_UP);

        // Convert the BigDecimal to a long
        return amountInPaisaBigDecimal.longValueExact(); // Use longValueExact to ensure no data loss
    }
}

