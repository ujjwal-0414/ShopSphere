package com.ujjwal.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(max=100)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max=100)
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter valid email address")
    private String email;

    @NotBlank(message = "Password must be at least 8 characters long")
    @Size(min = 8, max = 100,message = "Password must be between 8 and 100 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private String phoneNumber;

}
