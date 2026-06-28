package com.servicedesk.servicedesk_pro.controller;

import com.servicedesk.servicedesk_pro.dto.CommentResponse;
import com.servicedesk.servicedesk_pro.dto.CreateCommentRequest;
import com.servicedesk.servicedesk_pro.model.Comment;
import com.servicedesk.servicedesk_pro.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{ticketId}/comments")
    public CommentResponse addComment(@PathVariable  Long ticketId,
                                      @Valid @RequestBody CreateCommentRequest request)  {
        return commentService.addComment(ticketId,request);
    }

    @GetMapping("/{ticketId}/comments")
    public List<CommentResponse> getCommentsByTicket(@PathVariable Long ticketId){
        return commentService.getCommentsByTicket(ticketId);
    }
}
