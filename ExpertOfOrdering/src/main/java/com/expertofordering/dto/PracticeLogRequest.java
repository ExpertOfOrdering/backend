package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PracticeLogRequest {

    @Schema(description = "Year and month of the practice", example = "2023-10")
    @NotBlank
    private String yearMonth;

    @Schema(description = "Duration of the practice in seconds", example = "45")
    @NotNull
    private Integer durationSec;

    @Schema(description = "Order accuracy percentage", example = "95")
    @NotNull
    @Min(0)
    @Max(100)
    private Integer accuracy;
}
