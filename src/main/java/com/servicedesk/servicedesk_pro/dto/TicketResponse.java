package com.servicedesk.servicedesk_pro.dto;

import com.servicedesk.servicedesk_pro.enums.TicketPriority;
import com.servicedesk.servicedesk_pro.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String title,
        String description,
        TicketPriority priority,
        TicketStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdById,
        String createdByName,
        Long assignedToId,
        String assignedToName
){}
