package com.ceos22.cgv_clone.domain.movie.controller;

import com.ceos22.cgv_clone.domain.movie.dto.response.MovieResponseDto;
import com.ceos22.cgv_clone.domain.theater.dto.response.ShowtimeResponseDto;
import com.ceos22.cgv_clone.domain.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    public List<MovieResponseDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/movies/{id}")
    public MovieResponseDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // 영화 + 극장 상영 시간표 조회
    @GetMapping("/movies/{movieId}/theaters/{theaterId}/showtimes")
    public List<ShowtimeResponseDto> getShowtimes(@PathVariable Long movieId, @PathVariable Long theaterId) {
        return movieService.getShowtimesByMovieAndTheater(movieId, theaterId);
    }
}
