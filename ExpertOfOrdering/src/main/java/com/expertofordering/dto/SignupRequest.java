package com.expertofordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Sign up request")
public record SignupRequest(
        @Schema(description = "Display name", example = "아기사자")
        @NotBlank
        String name,

        @Schema(description = "Username (4~20 chars)", example = "test")
        @NotBlank
        @Size(min = 4, max = 20)
        String username,

        @Schema(description = "Password (8~72 chars)", example = "password1234")
        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {}
