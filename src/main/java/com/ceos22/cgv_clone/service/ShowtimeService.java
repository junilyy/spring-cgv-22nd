package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.dto.ticket.ShowtimeResponseDto;
import com.ceos22.cgv_clone.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;

    //특정 극장의 상영 시간표 (영화 여러개)
    public List<ShowtimeResponseDto> getShowtimesByTheater(Long theaterId) {
        return showtimeRepository.findByScreen_Theater_Id(theaterId).stream()
                .map(s -> new ShowtimeResponseDto(
                        s.getId(),
                        s.getMovie().getTitle(),
                        s.getScreen().getName(),
                        s.getScreen().getType(),
                        s.getDate().toString(),
                        s.getStartTime().toString(),
                        s.getEndTime().toString()
                ))
                .toList();
    }

    // 특정 영화 + 극장의 상영 시간표
    public List<ShowtimeResponseDto> getShowtimesByMovieAndTheater(Long movieId, Long theaterId) {
        return showtimeRepository.findByMovie_IdAndScreen_Theater_Id(movieId, theaterId).stream()
                .map(s -> new ShowtimeResponseDto(
                        s.getId(),
                        s.getMovie().getTitle(),
                        s.getScreen().getName(),
                        s.getScreen().getType(),
                        s.getDate().toString(),
                        s.getStartTime().toString(),
                        s.getEndTime().toString()
                ))
                .toList();
    }
}
