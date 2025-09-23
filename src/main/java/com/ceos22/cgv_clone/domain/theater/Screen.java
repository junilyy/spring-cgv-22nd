package com.ceos22.cgv_clone.domain.theater;

import com.ceos22.cgv_clone.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "screen")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screen extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id")
    private Long id;

    // 1관, 2관
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String type; // 일반관/특별관

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Builder
    public Screen(String name, String type, Theater theater) {
        this.name = name;
        this.type = type;
        this.theater = theater;
    }
}
