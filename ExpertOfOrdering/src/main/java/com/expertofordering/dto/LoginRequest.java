package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request")
public record LoginRequest(
        @Schema(description = "Username", example = "test")
        @NotBlank
        String username,

        @Schema(description = "Password", example = "password1234")
        @NotBlank
        String password
) {}
