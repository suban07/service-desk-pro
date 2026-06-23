package com.servicedesk.servicedesk_pro.repository;

import com.servicedesk.servicedesk_pro.enums.TicketPriority;
import com.servicedesk.servicedesk_pro.enums.TicketStatus;
import com.servicedesk.servicedesk_pro.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByPriority(TicketPriority priority);
    List<Ticket> findByAssignedToId(Long Id);
    List<Ticket> findByCreatedById(Long Id);
}
