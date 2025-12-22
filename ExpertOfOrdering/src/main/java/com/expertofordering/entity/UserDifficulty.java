package com.expertofordering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDifficulty {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty currentRecommendedDifficulty;

    public void updateDifficulty(Difficulty newDifficulty) {
        this.currentRecommendedDifficulty = newDifficulty;
    }
}
