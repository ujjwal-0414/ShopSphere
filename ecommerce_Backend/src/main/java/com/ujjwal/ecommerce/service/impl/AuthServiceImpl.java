package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.LoginRequest;
import com.ujjwal.ecommerce.dto.request.RegisterRequest;
import com.ujjwal.ecommerce.dto.response.AuthResponse;
import com.ujjwal.ecommerce.dto.response.UserResponse;
import com.ujjwal.ecommerce.entity.Role;
import com.ujjwal.ecommerce.entity.User;
import com.ujjwal.ecommerce.enums.RoleType;
import com.ujjwal.ecommerce.exception.ConflictException;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.exception.UnauthorizedException;
import com.ujjwal.ecommerce.mapper.AuthMapper;
import com.ujjwal.ecommerce.repository.RoleRepository;
import com.ujjwal.ecommerce.repository.UserRepository;
import com.ujjwal.ecommerce.security.CustomUserDetails;
import com.ujjwal.ecommerce.security.JwtService;
import com.ujjwal.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService  jwtService;
    private final AuthMapper authMapper;

    public UserResponse register(RegisterRequest registerRequest) {
        if(userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        // fetching default user role
        Role role = roleRepo.findByName(RoleType.USER)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Default USER role not found"
                ));

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        // securely hashing the password before saving into the database
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(role);

        User savedUser =  userRepo.save(user);

        // converting user entity into dto user response
        return authMapper.mapToUserResponse(savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            // Authenticate user credentials
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            // Explicitly returning 401 Unauthorized instead of 403 Forbidden
            throw new UnauthorizedException("Invalid email or password");
        }
        //Extract CustomUserDetails from principal
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        //Generate token
        String token = jwtService.generateToken(customUserDetails);

        //Get User directly from CustomUserDetails (NO extra database call required!)
        User user = customUserDetails.getUser();

        //Construct and return response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("Login successful");
        authResponse.setUser(authMapper.mapToUserResponse(user));

        return authResponse;
    }

}
