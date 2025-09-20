package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class TicketRequestDto {
    private Long showtimeId;
    private Long userId;
    private Integer generalCount;
    private Integer youthCount;
    private List<String> seatNumbers;
}
