package com.ceos22.cgv_clone.domain.movie;

import com.ceos22.cgv_clone.domain.BaseEntity;
import com.ceos22.cgv_clone.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "ratng", nullable = false)
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Builder
    private Review(String content, Integer rating, User user, Movie movie) {
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.movie = movie;
    }
}
