package com.ceos22.cgv_clone.domain.theater;

import com.ceos22.cgv_clone.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Seat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "screen_id")
    private Screen screen;

    @Column(length = 50)
    private String seat_row; // ex) A, B

    @Column(length = 50)
    private String seat_col; // ex) 1, 2
}
