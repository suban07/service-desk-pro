package com.servicedesk.servicedesk_pro.dto;

import com.servicedesk.servicedesk_pro.enums.UserRole;

public record UpdateUserRequest(
        String name,
        String email,
        UserRole role
) {
}
