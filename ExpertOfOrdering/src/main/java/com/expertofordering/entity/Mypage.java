package com.expertofordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Mypage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 2025-10 이런식으로 저장함
    private String yearMonth;

    private int practiceCount;

    private int avgSuccessRate;

    private int avgDurationSec;

    @Enumerated(EnumType.STRING)
    private Difficulty recommendDifficulty;


}
