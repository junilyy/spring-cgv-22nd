package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.request.OrderRequestDto;
import com.ceos22.cgv_clone.dto.response.OrderResponseDto;
import com.ceos22.cgv_clone.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }
}
