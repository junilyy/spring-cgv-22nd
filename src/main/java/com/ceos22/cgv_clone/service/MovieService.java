package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.*;
import com.ceos22.cgv_clone.dto.movie.*;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final PosterRepository posterRepository;
    private final MoviePersonRepository moviePersonRepository;
    private final EventRepository eventRepository;
    private final TrailerRepository trailerRepository;

    //홈 화면 영화 정보
    public List<MovieHomeDto> getHomeMovies() {
        return movieRepository.findAll().stream()
                .map(movie -> MovieHomeDto.builder()
                        .movieId(movie.getId())
                        .title(movie.getTitle())
                        .ageLimit(movie.getAgeLimit())
                        .bookingRate(movie.getBookingRate())
                        .releaseDate(movie.getReleaseDate())
                        .totalAudience(movie.getTotalAudience())
                        .eggNum(movie.getEggNum())
                        .posterUrl(
                                posterRepository.findByMovieId(movie.getId()).isEmpty() ? null : posterRepository.findByMovieId(movie.getId()).get(0).getImageUrl()
                        )
                        .build()
                ).toList();
    }

    //영화 상세 조회
    public MovieDetailDto getMovieDetail(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없음"));

        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        List<Poster> posters = posterRepository.findByMovieId(movieId);
        List<MoviePerson> persons = moviePersonRepository.findByMovieId(movieId);
        List<Event> events = eventRepository.findByMovieId(movieId);
        List<Trailer> trailers = trailerRepository.findByMovieId(movieId);

        return MovieDetailDto.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .ageLimit(movie.getAgeLimit())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .genre(movie.getGenre())
                .bookingRate(movie.getBookingRate())
                .totalAudience(movie.getTotalAudience())
                .eggNum(movie.getEggNum())
                .prologue(movie.getPrologue())
                .persons(persons.stream()
                        .map(p -> new MoviePersonDto(p.getId(), p.getName(), p.getRoleType(), p.getImageUrl()))
                        .toList())
                .posters(posters.stream()
                        .map(p -> new PosterDto(p.getId(), p.getImageUrl()))
                        .toList())
                .events(events.stream()
                        .map(e -> new EventDto(
                                e.getId(), e.getTitle(), e.getDescription(),
                                e.getStartDate(), e.getEndDate(), e.getImageUrl()
                        ))
                        .toList())
                .reviews(reviews.stream()
                        .map(r -> new ReviewDto(
                                r.getId(), r.getContent(), r.getRating(),
                                r.getCreatedAt(), r.getUser().getUsername()
                        ))
                        .toList())
                .trailers(trailers.stream()
                        .map(t -> new TrailerDto(t.getId(), t.getVideoUrl(), t.getDescription()))
                        .toList())
                .build();
    }
}
