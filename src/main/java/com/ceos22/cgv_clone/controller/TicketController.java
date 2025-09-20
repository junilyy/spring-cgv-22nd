package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.ticket.*;
import com.ceos22.cgv_clone.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/reserve")
    public ResponseEntity<TicketResponseDto> reserve(@RequestBody TicketRequestDto request) {
        return ResponseEntity.ok(ticketService.reserveTicket(request));
    }

    @DeleteMapping("/cancel/{ticketId}")
    public ResponseEntity<String> cancel(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
        return ResponseEntity.ok("예매가 취소되었습니다.");
    }
}
