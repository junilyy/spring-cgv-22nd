package com.ceos22.cgv_clone.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
