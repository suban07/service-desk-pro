package com.servicedesk.servicedesk_pro.dto;

import com.servicedesk.servicedesk_pro.enums.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        UserRole role,
        LocalDateTime createdAt
) {
}
