package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login response (no JWT)")
public record AuthResponse(
        @Schema(description = "User id", example = "1")
        Long userId,

        @Schema(description = "Display name", example = "김순자")
        String name,

        @Schema(description = "Username", example = "test")
        String username
) {}
