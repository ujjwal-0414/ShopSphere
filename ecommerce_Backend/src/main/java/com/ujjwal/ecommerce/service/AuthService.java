package com.ujjwal.ecommerce.service;

import com.ujjwal.ecommerce.dto.request.LoginRequest;
import com.ujjwal.ecommerce.dto.request.RegisterRequest;
import com.ujjwal.ecommerce.dto.response.AuthResponse;
import com.ujjwal.ecommerce.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);

}
