package com.ceos22.cgv_clone.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id", nullable = false)
    private Price price;
    */

    //A1, B12...
    @Column(name = "seat_numbers", nullable = false, length = 10)
    private String seatNumbers;

    @Column(name = "general_count", nullable = false)
    private Integer generalCount;

    @Column(name = "youth_count", nullable = false)
    private Integer youthCount;

    @Column(name = "final_price", nullable = false)
    private Integer finalPrice;
}
