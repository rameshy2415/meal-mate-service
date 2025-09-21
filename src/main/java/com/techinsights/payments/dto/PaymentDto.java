package com.techinsights.payments.dto;

import lombok.Data;

@Data
public class PaymentDto {

    private String razorpay_order_id;
    private String razorpay_payment_id;
    private String razorpay_signature;
}
