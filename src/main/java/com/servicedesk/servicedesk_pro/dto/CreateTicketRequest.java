package com.servicedesk.servicedesk_pro.dto;

import com.servicedesk.servicedesk_pro.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequest (

        @NotBlank(message = "Title is Required")
         String title,
        @NotBlank(message = "description is Required")
        String description,
        @NotNull(message = "Priority is Required")
        TicketPriority priority,
        @NotNull(message = "CreatedById is Required")
        Long createdById
) {

}
