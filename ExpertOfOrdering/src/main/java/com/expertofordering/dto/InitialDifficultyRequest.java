package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InitialDifficultyRequest {

    @Schema(description = "ID of the user", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "Score from the initial test (e.g., 0-10)", example = "8")
    @NotNull
    private Integer score;
}
