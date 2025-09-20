package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.ticket.ShowtimeResponseDto;
import com.ceos22.cgv_clone.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ShowtimeResponseDto>> getShowtimesByTheater(@PathVariable Long theaterId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByTheater(theaterId));
    }

    @GetMapping("/movie/{movieId}/theater/{theaterId}")
    public ResponseEntity<List<ShowtimeResponseDto>> getShowtimesByMovieAndTheater(
            @PathVariable Long movieId,
            @PathVariable Long theaterId
    ) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovieAndTheater(movieId, theaterId));
    }
}
