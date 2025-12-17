package com.expertofordering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank String name,
        @NotBlank @Size(min = 4, max = 20) String username,
        @NotBlank @Size(min = 8, max = 72) String password
) {}
