package com.ceos22.cgv_clone.domain.theater;

import com.ceos22.cgv_clone.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "theater")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Theater extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(columnDefinition = "TEXT")
    private String accessInfo;

    @Column(columnDefinition = "TEXT")
    private String parkingInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private String region; // 서울/경기/...
}
