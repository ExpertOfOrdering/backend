package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login response containing JWT access token")
public record AuthResponse(
        @Schema(
                description = "JWT access token (put this into Authorization: Bearer <token>)",
                example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0.abc..."
        )
        String accessToken
) {}
