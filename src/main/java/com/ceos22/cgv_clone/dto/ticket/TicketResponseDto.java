package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

@Getter
@AllArgsConstructor
public class TicketResponseDto {
    private Long ticketId;
    private Long showtimeId;
    private String seatNumbers;
    private Integer generalCount;
    private Integer youthCount;
    private Integer finalPrice;
}
