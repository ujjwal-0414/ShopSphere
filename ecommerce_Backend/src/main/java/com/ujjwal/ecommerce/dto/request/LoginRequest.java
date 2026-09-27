package com.ujjwal.ecommerce.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter valid email address")
    private String email;

    @NotBlank(message = "Password must be at least 8 characters long")
    @Size(min = 8, max = 100,message = "Password must be between 8 and 100 characters")
    private String password;

}
