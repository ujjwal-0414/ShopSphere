package com.ujjwal.ecommerce.mapper;

import com.ujjwal.ecommerce.dto.response.UserResponse;
import com.ujjwal.ecommerce.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole().getName());

        return response;
    }

}
