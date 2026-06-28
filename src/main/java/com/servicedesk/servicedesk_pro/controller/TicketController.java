package com.servicedesk.servicedesk_pro.controller;

import com.servicedesk.servicedesk_pro.dto.CreateTicketRequest;
import com.servicedesk.servicedesk_pro.dto.TicketResponse;
import com.servicedesk.servicedesk_pro.dto.UpdateTicketStatusRequest;
import com.servicedesk.servicedesk_pro.enums.TicketPriority;
import com.servicedesk.servicedesk_pro.enums.TicketStatus;
import com.servicedesk.servicedesk_pro.model.Ticket;
import com.servicedesk.servicedesk_pro.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService){
        this.ticketService=ticketService;
    }

    @PostMapping
    public TicketResponse createTicket(@Valid @RequestBody CreateTicketRequest request)  {
        return ticketService.createTicket(request);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets(){
        return ticketService.getAllTickets();
    }

    @PutMapping("/{ticketId}/assign/{engineerId}")
    public TicketResponse assignTicket(@PathVariable Long ticketId,
                               @PathVariable Long engineerId) {
        return ticketService.assignTicket(ticketId,engineerId);
    }

    @PutMapping("/{ticketId}/status")
    public TicketResponse updateTicketStatus(@PathVariable Long ticketId,
                                     @Valid @RequestBody UpdateTicketStatusRequest request) {
        return ticketService.updateTicketStatus(ticketId,request);
    }

    @GetMapping("/{ticketId}")
    public TicketResponse getTicketById(@PathVariable Long ticketId) {
        return ticketService.getTicketById(ticketId);
    }

    @PutMapping("/{ticketId}/close")
    public TicketResponse closeTicket(@PathVariable Long ticketId) {
        return ticketService.closeTicket(ticketId);
    }

    @GetMapping("/status/{status}")
    public List<TicketResponse> getTicketsByStatus(@PathVariable TicketStatus status) {
        return ticketService.getTicketsByStatus(status);
    }

    @GetMapping("/priority/{priority}")
    public List<TicketResponse> getTicketsByPriority(@PathVariable TicketPriority priority) {
        return ticketService.getTicketsByPriority(priority);
    }

    @GetMapping("/assigned/{engineerId}")
    public List<TicketResponse> getTicketsByEngineerId(
            @PathVariable Long engineerId) {

        return ticketService.getTicketsByEngineerId(engineerId);
    }

    @GetMapping("/customer/{customerId}")
    public List<TicketResponse> getTicketsByCustomeId(
            @PathVariable Long customerId) {

        return ticketService.getTicketsByCustomerId(customerId);
    }

    @GetMapping("/page")
    public Page<TicketResponse> getTicketsWithPagination(
            @RequestParam int page,
            @RequestParam int size) {

        return ticketService.getTicketsWithPagination(page, size);
    }

    @GetMapping("/page-sort")
    public Page<TicketResponse> getTicketsWithPaginationAndSorting(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy,
            @RequestParam String direction) {

        return ticketService.getTicketsWithPaginationAndSorting(
                page, size, sortBy, direction);
    }

}
