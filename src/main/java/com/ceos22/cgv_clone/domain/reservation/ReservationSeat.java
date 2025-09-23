package com.ceos22.cgv_clone.domain.reservation;

import com.ceos22.cgv_clone.domain.BaseEntity;
import com.ceos22.cgv_clone.domain.theater.Seat;
import com.ceos22.cgv_clone.domain.theater.Showtime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_seat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id")
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private ReservationStatus status; // RESERVED / AVAILABLE

    @Builder
    public ReservationSeat(Showtime showtime, Seat seat, Ticket ticket, ReservationStatus status) {
        this.showtime = showtime;
        this.seat = seat;
        this.ticket = ticket;
        this.status = status;
    }

    //예약 상태 변경을 위한 메서드
    public void cancel(){
        this.status = ReservationStatus.AVAILABLE;
    }

    public void reserve(){
        this.status = ReservationStatus.RESERVED;
    }

}
