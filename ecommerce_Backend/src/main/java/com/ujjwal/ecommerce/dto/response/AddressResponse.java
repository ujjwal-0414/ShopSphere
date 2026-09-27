package com.ujjwal.ecommerce.dto.response;

import com.ujjwal.ecommerce.enums.AddressType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressResponse {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String addressLine;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private AddressType addressType;

    private Boolean isDefault;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
