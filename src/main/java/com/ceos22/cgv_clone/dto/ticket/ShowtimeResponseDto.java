package com.ceos22.cgv_clone.dto.ticket;

import lombok.*;

@Getter
@AllArgsConstructor
public class ShowtimeResponseDto {
    private Long showtimeId;
    private String movieTitle;
    private String screenName;
    private String screenType;
    private String date;
    private String startTime;
    private String endTime;
}