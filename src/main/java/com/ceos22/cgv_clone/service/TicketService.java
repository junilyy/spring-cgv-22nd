package com.ceos22.cgv_clone.service;

import com.ceos22.cgv_clone.domain.*;
import com.ceos22.cgv_clone.dto.ticket.*;
import com.ceos22.cgv_clone.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;

    //예매
    @Transactional
    public TicketResponseDto reserveTicket(TicketRequestDto request) {
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new IllegalArgumentException("상영시간표 없음"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        // 가격 계산(개선 필요)
        int finalPrice = request.getGeneralCount() * 12000
                + request.getYouthCount() * 10000;

        // 좌석 문자열 변환
        String seats = String.join(",", request.getSeatNumbers());

        // 중복 예매 체크는 불필요..?

        // 예매 정보 저장
        Ticket ticket = Ticket.builder()
                .showtime(showtime)
                .user(user)
                .seatNumbers(seats)
                .generalCount(request.getGeneralCount())
                .youthCount(request.getYouthCount())
                .finalPrice(finalPrice)
                .build();

        Ticket saved = ticketRepository.save(ticket);

        // 예매 정보 반환
        return new TicketResponseDto(
                saved.getId(),
                saved.getShowtime().getId(),
                saved.getSeatNumbers(),
                saved.getGeneralCount(),
                saved.getYouthCount(),
                saved.getFinalPrice()
        );
    }

    //예매 취소
    @Transactional
    public void cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓 없음"));
        ticketRepository.delete(ticket);
    }
}
