package com.servicedesk.servicedesk_pro.dto;

import com.servicedesk.servicedesk_pro.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusRequest(
        @NotNull(message = "status message is required")
        TicketStatus status
) {
}
