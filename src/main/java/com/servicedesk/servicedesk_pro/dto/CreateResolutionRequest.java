package com.servicedesk.servicedesk_pro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateResolutionRequest (
        @NotBlank(message = "Route Cause message is required")
        String rootCause,
        @NotBlank(message = "Solution message is required")
        String solution,
        @NotNull(message = "ResolvedById is required")
        Long resolvedById
){

}
