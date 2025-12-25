package com.expertofordering.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Standard error response")
public record ApiError(
        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Short error code for client handling", example = "VALIDATION_ERROR")
        String code,

        @Schema(description = "Human-readable message", example = "Validation failed")
        String message,

        @Schema(description = "Request path", example = "/api/auth/signup")
        String path,

        @Schema(description = "Timestamp (UTC)", example = "2025-12-25T17:24:05Z")
        Instant timestamp,

        @Schema(description = "Field-level errors (optional)")
        List<FieldError> errors
) {
    @Schema(description = "Field error detail")
    public record FieldError(
            @Schema(description = "Field name", example = "username")
            String field,

            @Schema(description = "Rejected value (may be null)", example = "a")
            Object rejectedValue,

            @Schema(description = "Error message", example = "size must be between 4 and 20")
            String message
    ) {}
}
