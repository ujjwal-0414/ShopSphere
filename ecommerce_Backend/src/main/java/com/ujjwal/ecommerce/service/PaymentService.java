package com.ujjwal.ecommerce.service;

import com.ujjwal.ecommerce.dto.request.PaymentRequest;
import com.ujjwal.ecommerce.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse getPaymentByOrderId(Long orderId);

}
