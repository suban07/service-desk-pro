package com.servicedesk.servicedesk_pro.repository;

import com.servicedesk.servicedesk_pro.model.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResolutionRepository extends JpaRepository<Resolution,Long> {
    Optional<Resolution> findByTicketId(Long ticketId);
}
