package com.ceos22.cgv_clone.domain.shop.controller;

import com.ceos22.cgv_clone.domain.shop.dto.request.OrderRequestDto;
import com.ceos22.cgv_clone.domain.shop.dto.response.OrderResponseDto;
import com.ceos22.cgv_clone.domain.shop.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication") // JWT 인증 필요
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(userDetails.getUsername(), orderRequestDto);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long orderId) {
        return orderService.getOrder(userDetails.getUsername(), orderId);
    }
}
