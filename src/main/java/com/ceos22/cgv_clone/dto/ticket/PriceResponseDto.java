package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

@Getter
@AllArgsConstructor
public class PriceResponseDto {
    private String movieType;
    private String dayType;
    private String timeSlot;
    private Integer generalPrice;
    private Integer youthPrice;
}
