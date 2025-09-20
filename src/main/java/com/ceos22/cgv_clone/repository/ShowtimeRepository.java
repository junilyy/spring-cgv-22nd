package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    // 극장 전체 상영시간표
    List<Showtime> findByScreen_Theater_Id(Long theaterId);

    // 특정 영화 + 극장 상영시간표
    List<Showtime> findByMovie_IdAndScreen_Theater_Id(Long movieId, Long theaterId);
}
