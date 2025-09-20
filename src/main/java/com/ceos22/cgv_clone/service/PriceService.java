package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.dto.ticket.PriceResponseDto;
import com.ceos22.cgv_clone.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;
    
    //특정 극장의 가격표
    public List<PriceResponseDto> getPricesByTheater(Long theaterId) {
        return priceRepository.findByTheater_Id(theaterId).stream()
                .map(p -> new PriceResponseDto(
                        p.getMovieType(),
                        p.getDayType(),
                        p.getTimeSlot(),
                        p.getGeneralPrice(),
                        p.getYouthPrice()
                ))
                .toList();
    }
}
