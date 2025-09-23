package com.ceos22.cgv_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoviePersonRepository extends JpaRepository<MoviePerson, Long> {
    List<MoviePerson> findByMovieId(Long movieId);
}