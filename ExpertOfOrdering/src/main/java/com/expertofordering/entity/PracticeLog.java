package com.expertofordering.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PracticeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 2025-10 이런식으로 저장함
    @Column(name = "log_year_month")
    private int month;
    private int DurationSec;

    // 주문 정확도 (0 ~ 100%)
    private int accuracy;
}
