package com.ujjwal.ecommerce.dto.response;

import com.ujjwal.ecommerce.enums.RoleType;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private RoleType role;

}
