package com.ceos22.cgv_clone.domain.reservation.service;

import com.ceos22.cgv_clone.domain.reservation.entity.ReservationStatus;
import com.ceos22.cgv_clone.domain.reservation.entity.TicketPrice;
import com.ceos22.cgv_clone.domain.reservation.repository.ReservationSeatRepository;
import com.ceos22.cgv_clone.domain.reservation.repository.TicketRepository;
import com.ceos22.cgv_clone.domain.theater.repository.ShowtimeRepository;
import com.ceos22.cgv_clone.domain.user.entity.User;
import com.ceos22.cgv_clone.domain.reservation.entity.ReservationSeat;
import com.ceos22.cgv_clone.domain.reservation.entity.Ticket;
import com.ceos22.cgv_clone.domain.theater.entity.Showtime;
import com.ceos22.cgv_clone.domain.user.repository.UserRepository;
import com.ceos22.cgv_clone.domain.reservation.dto.request.TicketRequestDto;
import com.ceos22.cgv_clone.domain.reservation.dto.response.TicketResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ceos22.cgv_clone.global.redis.RedissonLockHelper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ShowtimeRepository showtimeRepository;
    private final UserRepository userRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final RedissonLockHelper lockHelper;

    // 락 키 유틸
    private String seatKey(Long showtimeId, String row, String col) {
        return "seat:%d:%s-%s".formatted(showtimeId, row, col);
    }

    // 예매
    @Transactional
    public TicketResponseDto reserveTicket(String username, TicketRequestDto request) {
        int seatCount = request.getSeatNumbers() == null ? 0 : request.getSeatNumbers().size();
        log.debug("[SVC] reserveTicket start - user={}, showtime={}, seats={}",
                username, request.getShowtimeId(), seatCount);

        try {
            Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                    .orElseThrow(() -> new IllegalArgumentException("상영시간표 없음"));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

            // 최종 결제 금액 계산(!!!!!!계산 로직 분리하기!!!!!!!)
            int finalPrice = request.getGeneralCount() * TicketPrice.GENERAL.getPrice()
                    + request.getYouthCount() * TicketPrice.YOUTH.getPrice();

            // 이번 예약의 좌석 키들을 수집
            List<String> lockKeys = request.getSeatNumbers().stream()
                    .map(seat -> {
                        String row = seat.substring(0, 1);
                        String col = seat.substring(1);
                        return seatKey(request.getShowtimeId(), row, col);
                    })
                    .distinct()
                    .toList();

            // 잠금(lock) 실행
            return lockHelper.withLocks(lockKeys, 2000, 5000, () -> {
                // 좌석 예매
                List<ReservationSeat> reservedSeats = new ArrayList<>();

                for (String seatNumber : request.getSeatNumbers()) {
                    String row = seatNumber.substring(0, 1);
                    String col = seatNumber.substring(1);

                    boolean alreadyReserved = reservationSeatRepository
                            .existsByShowtime_IdAndSeatRowAndSeatColAndStatus(request.getShowtimeId(), row, col, ReservationStatus.RESERVED);

                    if (alreadyReserved) {
                        throw new IllegalStateException("이미 예약된 좌석입니다.");
                    }

                    ReservationSeat newReservation = ReservationSeat.builder()
                            .showtime(showtime)
                            .seatRow(row)
                            .seatCol(col)
                            .status(ReservationStatus.RESERVED)
                            .build();

                    reservedSeats.add(reservationSeatRepository.save(newReservation));
                }

                // Ticket 저장
                Ticket ticket = Ticket.builder()
                        .showtime(showtime)
                        .user(user)
                        .generalCnt(request.getGeneralCount())
                        .youthCnt(request.getYouthCount())
                        .finalPrice(finalPrice)
                        .build();

                Ticket savedTicket = ticketRepository.save(ticket);

                // 좌석에 티켓 연결
                for (ReservationSeat rs : reservedSeats) {
                    rs.setTicket(savedTicket);
                }

                List<String> rsSeats = reservedSeats.stream().map(rs -> rs.getSeatRow() + rs.getSeatCol()).toList();

                log.info("[SVC] 예매 완료 - user={}, showtime={}, ticketId={}, seats={}, price={}",
                        username, request.getShowtimeId(), savedTicket.getId(), rsSeats.size(), finalPrice);

                return TicketResponseDto.fromEntity(savedTicket, rsSeats);
            });
        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(예매) - user={}, showtime={}, msg={}",
                    username, request.getShowtimeId(), e.getMessage());
            throw e;
        }
        catch (IllegalStateException e) {
            log.warn("[SVC] 비정상 좌석 요청(중복 등) - user={}, showtime={}, msg={}",
                    username, request.getShowtimeId(), e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 예매 처리 실패 - user={}, showtime={}", username, request.getShowtimeId(), e);
            throw e;
        }
    }

    // 예매 취소
    @Transactional
    public void cancelTicket(String username, Long ticketId) {
        log.debug("[SVC] cancelTicket start - user={}, ticketId={}", username, ticketId);

        try {
            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new IllegalArgumentException("티켓 없음"));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

            if (!ticket.getUser().getId().equals(user.getId())) {
                throw new SecurityException("본인이 예매한 티켓만 취소할 수 있습니다.");
            }

            // 좌석 해제(삭제) → 티켓 삭제
            reservationSeatRepository.deleteByTicket_Id(ticket.getId());
            ticketRepository.delete(ticket);

            log.info("[SVC] 예매 취소 완료 - user={}, ticketId={}", username, ticketId);

        }
        catch (IllegalArgumentException e) {
            log.warn("[SVC] 잘못된 요청(취소) - user={}, ticketId={}, msg={}", username, ticketId, e.getMessage());
            throw e;
        }
        catch (SecurityException e) {
            log.warn("[SVC] 권한 없는 취소 시도 - user={}, ticketId={}, msg={}", username, ticketId, e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error("[SVC] 예매 취소 실패 - user={}, ticketId={}", username, ticketId, e);
            throw e;
        }
    }
}