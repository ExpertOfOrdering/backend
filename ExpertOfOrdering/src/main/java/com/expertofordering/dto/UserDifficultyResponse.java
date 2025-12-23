package com.expertofordering.dto;

import com.expertofordering.entity.Difficulty;
import com.expertofordering.entity.UserDifficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserDifficultyResponse {

    @Schema(description = "The user's current recommended difficulty", example = "중수")
    private final Difficulty currentRecommendedDifficulty;

    public UserDifficultyResponse(UserDifficulty userDifficulty) {
        this.currentRecommendedDifficulty = userDifficulty.getCurrentRecommendedDifficulty();
    }
}
