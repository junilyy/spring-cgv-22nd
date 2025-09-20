package com.ceos22.cgv_clone.repository;

import com.ceos22.cgv_clone.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByTheater_Id(Long theaterId);
}
