package com.ceos22.cgv_clone.domain.shop;

import com.ceos22.cgv_clone.domain.BaseEntity;
import com.ceos22.cgv_clone.domain.theater.Theater;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    private Theater theater;

    @Column(name = "stock", nullable = false)
    private int stock;
}
