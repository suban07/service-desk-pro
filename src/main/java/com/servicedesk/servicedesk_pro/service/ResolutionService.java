package com.servicedesk.servicedesk_pro.service;

import com.servicedesk.servicedesk_pro.dto.CreateResolutionRequest;
import com.servicedesk.servicedesk_pro.dto.ResolutionResponse;
import com.servicedesk.servicedesk_pro.enums.TicketStatus;
import com.servicedesk.servicedesk_pro.enums.UserRole;
import com.servicedesk.servicedesk_pro.exception.ResolutionNotFoundException;
import com.servicedesk.servicedesk_pro.exception.TicketNotFoundException;
import com.servicedesk.servicedesk_pro.exception.UserNotFoundException;
import com.servicedesk.servicedesk_pro.model.Resolution;
import com.servicedesk.servicedesk_pro.model.Ticket;
import com.servicedesk.servicedesk_pro.model.User;
import com.servicedesk.servicedesk_pro.repository.ResolutionRepository;
import com.servicedesk.servicedesk_pro.repository.TicketRepository;
import com.servicedesk.servicedesk_pro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ResolutionService {
    private final ResolutionRepository resolutionRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    public ResolutionService(ResolutionRepository resolutionRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.resolutionRepository = resolutionRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public ResolutionResponse addResolution(Long ticketId, CreateResolutionRequest request){

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new TicketNotFoundException("Ticket Not found"));

        User user = userRepository.findById(request.resolvedById())
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        if(user.getRole() != UserRole.SUPPORT_ENGINEER){
            throw new RuntimeException("Resolved by only SUPPORT_ENGINEER");
        }

        Resolution resolution = new Resolution();
        resolution.setTicket(ticket);
        resolution.setSolution(request.solution());
        resolution.setRootCause(request.rootCause());
        resolution.setResolvedBy(user);
        resolution.setResolvedAt(LocalDateTime.now());

        ticket.setStatus(TicketStatus.RESOLVED);
        ticket.setUpdatedAt(LocalDateTime.now());

        ticketRepository.save(ticket);
        return  mapToResolutionResponse(resolutionRepository.save(resolution));
    }

    public ResolutionResponse getResolutionByTicket(Long ticketId) {
        return mapToResolutionResponse(resolutionRepository.findByTicketId(ticketId)
                .orElseThrow(()->new ResolutionNotFoundException("Resolution not found")));
    }

    private ResolutionResponse mapToResolutionResponse(Resolution resolution){
        return new ResolutionResponse(
                resolution.getId(),
                resolution.getRootCause(),
                resolution.getSolution(),
                resolution.getTicket().getId(),
                resolution.getResolvedBy().getId(),
                resolution.getResolvedBy().getName(),
                resolution.getResolvedAt()
        );
    }

}
