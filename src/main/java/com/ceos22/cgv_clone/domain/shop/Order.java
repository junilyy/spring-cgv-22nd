package com.ceos22.cgv_clone.domain.shop;

import com.ceos22.cgv_clone.domain.BaseEntity;
import com.ceos22.cgv_clone.domain.User;
import com.ceos22.cgv_clone.domain.theater.Theater;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(name = "total_price")
    private int totalPrice;
}

