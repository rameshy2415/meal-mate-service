package com.techinsights.payments.service.impl;

import com.razorpay.Order;

import java.math.BigDecimal;

public interface IRazorpayService {

     Order createOrder(BigDecimal amount) throws Exception;

     boolean verifyPayment(String orderId, String paymentId, String signature) throws Exception;
}
