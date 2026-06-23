package com.servicedesk.servicedesk_pro.dto;

import java.time.LocalDateTime;

public record ResolutionResponse(

        Long id,
        String rootCause,
        String solution,
        Long ticketId,
        Long resolvedById,
        String resolvedByName,
        LocalDateTime resolvedAt
){}
