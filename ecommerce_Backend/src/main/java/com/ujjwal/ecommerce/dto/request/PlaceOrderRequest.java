package com.ujjwal.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    @NotNull(message = "Address ID is required")
    @Positive(message = "Address ID must be positive") // this prevents zero or negative value
    private Long addressId;

}
