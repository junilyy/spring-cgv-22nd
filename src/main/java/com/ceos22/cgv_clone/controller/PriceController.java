package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.ticket.PriceResponseDto;
import com.ceos22.cgv_clone.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<PriceResponseDto>> getPricesByTheater(@PathVariable Long theaterId) {
        return ResponseEntity.ok(priceService.getPricesByTheater(theaterId));
    }
}