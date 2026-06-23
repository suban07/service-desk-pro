package com.servicedesk.servicedesk_pro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentRequest(
        @NotBlank(message = "Comment message is required")
        String message,
        @NotNull(message = "CommentById is required")
        Long commentedById
){}
