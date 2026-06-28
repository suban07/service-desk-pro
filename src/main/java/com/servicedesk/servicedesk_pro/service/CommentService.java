package com.servicedesk.servicedesk_pro.service;

import com.servicedesk.servicedesk_pro.dto.CommentResponse;
import com.servicedesk.servicedesk_pro.dto.CreateCommentRequest;
import com.servicedesk.servicedesk_pro.enums.TicketStatus;
import com.servicedesk.servicedesk_pro.exception.TicketNotFoundException;
import com.servicedesk.servicedesk_pro.exception.UserNotFoundException;
import com.servicedesk.servicedesk_pro.model.Comment;
import com.servicedesk.servicedesk_pro.model.Ticket;
import com.servicedesk.servicedesk_pro.model.User;
import com.servicedesk.servicedesk_pro.repository.CommentRepository;
import com.servicedesk.servicedesk_pro.repository.TicketRepository;
import com.servicedesk.servicedesk_pro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    public CommentService(CommentRepository commentRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public CommentResponse addComment(Long ticketId, CreateCommentRequest request){
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new TicketNotFoundException("Ticket Not Found"));

        User commentedBy = userRepository.findById(request.commentedById())
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(ticket.getStatus() != TicketStatus.ASSIGNED &&
                ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new RuntimeException(
                    "Comments can only be added to assigned or in-progress tickets");
        }

        if(!commentedBy.getId().equals(ticket.getAssignedTo().getId())
                &&
                !commentedBy.getId().equals(ticket.getCreatedBy().getId())) {

            throw new RuntimeException(
                    "Only the assigned engineer or the customer who created the ticket can add comments");
        }

        if(ticket.getStatus().equals(TicketStatus.RESOLVED)){
            throw new RuntimeException("Cannot add comments because the ticket is already resolved");
        }
        if(ticket.getStatus().equals(TicketStatus.CLOSED)){
            throw new RuntimeException("Cannot add comments because the ticket is closed");
        }

        Comment comment = new Comment();
        comment.setCommentedBy(commentedBy);
        comment.setTicket(ticket);
        comment.setMessage(request.message());
        comment.setCreatedAt(LocalDateTime.now());

        return mapToCommentResponse(commentRepository.save(comment));
    }


    public List<CommentResponse> getCommentsByTicket(Long ticketId) {
        return commentRepository.findByTicketId(ticketId)
                .stream()
                .map(this::mapToCommentResponse)
                .toList();
    }

    private CommentResponse mapToCommentResponse(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getMessage(),
                comment.getCommentedBy().getId(),
                comment.getCommentedBy().getName(),
                comment.getTicket().getId(),
                comment.getCreatedAt()
        );
    }
}
