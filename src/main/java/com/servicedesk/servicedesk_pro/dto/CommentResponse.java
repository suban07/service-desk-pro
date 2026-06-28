package com.servicedesk.servicedesk_pro.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String message,
        Long commentedById,
        String commentedByName,
        Long ticketId,
        LocalDateTime createdAt
){}
