package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PracticeLogRequest {

    @Schema(description = "Month of the practice (1-12)", example = "11")
    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;

    @Schema(description = "Duration of the practice in seconds", example = "45")
    @NotNull
    private Integer durationSec;

    @Schema(description = "Order accuracy percentage", example = "95")
    @NotNull
    @Min(0)
    @Max(100)
    private Integer accuracy;
}
