package com.servicedesk.servicedesk_pro.service;

import com.servicedesk.servicedesk_pro.dto.CreateTicketRequest;
import com.servicedesk.servicedesk_pro.dto.TicketResponse;
import com.servicedesk.servicedesk_pro.dto.UpdateTicketStatusRequest;
import com.servicedesk.servicedesk_pro.enums.TicketPriority;
import com.servicedesk.servicedesk_pro.enums.TicketStatus;
import com.servicedesk.servicedesk_pro.enums.UserRole;
import com.servicedesk.servicedesk_pro.exception.TicketNotFoundException;
import com.servicedesk.servicedesk_pro.exception.UserNotFoundException;
import com.servicedesk.servicedesk_pro.model.Ticket;
import com.servicedesk.servicedesk_pro.model.User;
import com.servicedesk.servicedesk_pro.repository.TicketRepository;
import com.servicedesk.servicedesk_pro.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    public TicketResponse createTicket(CreateTicketRequest request)  {
        User createdBy = userRepository.findById(request.createdById())
                        .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if(createdBy.getRole()!=UserRole.CUSTOMER){
            throw new RuntimeException("Only Customer can create tickets");
        }
        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket.setCreatedBy(createdBy);
        return mapToTicketResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToTicketResponse)
                .toList();
    }

    public TicketResponse assignTicket(Long ticketId, Long engineerId)  {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new TicketNotFoundException("TIcket not found"));
        User engineer = userRepository.findById(engineerId)
                .orElseThrow(()->new UserNotFoundException("user not found"));

        if(engineer.getRole() != UserRole.SUPPORT_ENGINEER){
            throw new RuntimeException("Ticket can be assigned to only SUPPORT_ENGINEER");
        }

        if (ticket.getStatus() != TicketStatus.OPEN) {
            throw new RuntimeException("Only OPEN tickets can be assigned");
        }

        if (ticket.getStatus() == TicketStatus.ASSIGNED) {
            throw new RuntimeException("Ticket is already assigned");
        }

        ticket.setAssignedTo(engineer);
        ticket.setStatus(TicketStatus.ASSIGNED);
        ticket.setUpdatedAt(LocalDateTime.now());

        return mapToTicketResponse(ticketRepository.save(ticket));
    }


    public TicketResponse updateTicketStatus(Long ticketId, UpdateTicketStatusRequest request) {
        User user = userRepository.findById(request.updatedById())
                .orElseThrow(() -> new UserNotFoundException("User not found"));



        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()->new TicketNotFoundException("Ticket not found"));

        if(ticket.getAssignedTo() == null){
            throw new RuntimeException("Ticket is not assigned");
        }

        if(!ticket.getAssignedTo().getId().equals(user.getId())){
            throw new RuntimeException(
                    "Only assigned engineer can update ticket status");
        }

        if(user.getRole() != UserRole.SUPPORT_ENGINEER){
            throw new RuntimeException(
                    "Only SUPPORT_ENGINEER can update ticket status");
        }

        if(ticket.getStatus()==TicketStatus.CLOSED){
            throw new RuntimeException("Cannot update status because the ticket is closed");
        }
        ticket.setStatus(request.status());
        ticket.setUpdatedAt(LocalDateTime.now());



        return mapToTicketResponse(ticketRepository.save(ticket));
    }

    public TicketResponse getTicketById(Long ticketId)  {
        return mapToTicketResponse(ticketRepository.findById(ticketId)
                .orElseThrow(()->new TicketNotFoundException("Ticket Not found")));
    }


    public TicketResponse closeTicket(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new RuntimeException("Ticket is already closed");
        }

        if (ticket.getStatus() != TicketStatus.RESOLVED) {
            throw new RuntimeException("Only RESOLVED tickets can be closed");
        }

        ticket.setStatus(TicketStatus.CLOSED);
        ticket.setUpdatedAt(LocalDateTime.now());

        return mapToTicketResponse(ticketRepository.save(ticket));
    }

    public List<TicketResponse> getTicketsByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status)
                .stream()
                .map(this::mapToTicketResponse)
                .toList();
    }

    public List<TicketResponse> getTicketsByPriority(TicketPriority priority) {
        return ticketRepository.findByPriority(priority)
                .stream()
                .map(this::mapToTicketResponse)
                .toList();
    }

    public List<TicketResponse> getTicketsByEngineerId(Long Id){
        return ticketRepository.findByAssignedToId(Id)
                .stream()
                .map(this::mapToTicketResponse)
                .toList();
    }

    public List<TicketResponse> getTicketsByCustomerId(Long Id){
        return ticketRepository.findByCreatedById(Id)
                .stream()
                .map(this::mapToTicketResponse)
                .toList();
    }

    public Page<TicketResponse> getTicketsWithPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Ticket> ticketPage = ticketRepository.findAll(pageable);

        return ticketPage.map(this::mapToTicketResponse);
    }


    public Page<TicketResponse> getTicketsWithPaginationAndSorting(
            int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Ticket> ticketPage = ticketRepository.findAll(pageable);

        return ticketPage.map(this::mapToTicketResponse);
    }



    private TicketResponse mapToTicketResponse(Ticket ticket){
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getCreatedBy() != null ? ticket.getCreatedBy().getId() : null,
                ticket.getCreatedBy() != null ? ticket.getCreatedBy().getName() : null,
                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getId() : null,
                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : null
        );
    }


}
