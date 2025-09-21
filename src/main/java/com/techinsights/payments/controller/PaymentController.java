package com.techinsights.payments.controller;

import com.razorpay.Order;
import com.techinsights.payments.dto.PaymentDto;
import com.techinsights.payments.service.impl.IRazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PaymentController {

    private final IRazorpayService razorpayService;

    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestParam BigDecimal amount) {
        Map<String, Object> response = new HashMap<>();
        try {
            Order order = razorpayService.createOrder(amount);
            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("status", order.get("status"));
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PostMapping("/verify-payment")
    public Map<String, Object> verifyPayment(@RequestBody PaymentDto PaymentDto) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean isValid = razorpayService.verifyPayment(PaymentDto.getRazorpay_order_id(), PaymentDto.getRazorpay_payment_id(), PaymentDto.getRazorpay_signature());

            response.put("status", isValid ? "success" : "failure");
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }
}

