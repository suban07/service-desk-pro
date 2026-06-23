package com.servicedesk.servicedesk_pro.controller;

import com.servicedesk.servicedesk_pro.dto.CreateResolutionRequest;
import com.servicedesk.servicedesk_pro.dto.ResolutionResponse;
import com.servicedesk.servicedesk_pro.model.Resolution;
import com.servicedesk.servicedesk_pro.service.ResolutionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class ResolutionController {

    private final ResolutionService resolutionService;

    public ResolutionController(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    @PostMapping("{ticketId}/resolution")
    public ResolutionResponse addResolution(@PathVariable Long ticketId,
                                    @Valid @RequestBody CreateResolutionRequest request) {
        return resolutionService.addResolution(ticketId,request);
    }

    @GetMapping("/{ticketId}/resolution")
    public ResolutionResponse getResolutionByTicket(@PathVariable Long ticketId) {
        return resolutionService.getResolutionByTicket(ticketId);
    }
}
