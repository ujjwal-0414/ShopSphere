package com.ujjwal.ecommerce.dto.request;

import com.ujjwal.ecommerce.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Order Id is required")
    @Positive(message = "Order Id must be positive")
    private Long orderId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

}
