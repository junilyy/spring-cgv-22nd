package com.ceos22.cgv_clone.controller;

import com.ceos22.cgv_clone.dto.movie.MovieHomeDto;
import com.ceos22.cgv_clone.dto.movie.MovieDetailDto;
import com.ceos22.cgv_clone.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<MovieHomeDto> getHomeMovies() {
        return movieService.getHomeMovies();
    }

    @GetMapping("/{id}")
    public MovieDetailDto getMovieDetail(@PathVariable Long id) {
        return movieService.getMovieDetail(id);
    }
}
