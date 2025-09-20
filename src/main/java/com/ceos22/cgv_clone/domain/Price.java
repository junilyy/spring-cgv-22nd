package com.ceos22.cgv_clone.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    //2D, 3D
    @Column(name = "movie_type", nullable = false, length = 10)
    private String movieType;

    //월~목, 금~일(공휴일)
    @Column(name = "day_type", nullable = false, length = 10)
    private String dayType;

    @Column(name = "time_slot", nullable = false, length = 50)  // 모닝/브런치/일반/심야
    private String timeSlot;

    @Column(name = "general_price", nullable = false)
    private Integer generalPrice;

    @Column(name = "youth_price", nullable = false)
    private Integer youthPrice;
}
