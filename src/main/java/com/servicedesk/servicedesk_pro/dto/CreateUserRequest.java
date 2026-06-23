package com.servicedesk.servicedesk_pro.dto;

import com.servicedesk.servicedesk_pro.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is Required")
        @Email(message = "Invalid Email Format")
        String email,

        @NotNull(message = "Role is required")
        UserRole role

){}
