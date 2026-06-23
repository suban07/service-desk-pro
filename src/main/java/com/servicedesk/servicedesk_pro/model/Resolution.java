package com.servicedesk.servicedesk_pro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="resolution")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resolution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rootCause;
    private String solution;
    private LocalDateTime resolvedAt;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "resolved_by_id")
    private User resolvedBy;
}
