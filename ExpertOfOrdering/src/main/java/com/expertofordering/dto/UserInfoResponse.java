package com.expertofordering.dto;

import com.expertofordering.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User info response")
public record UserInfoResponse(
        @Schema(description = "User display name", example = "홍길동")
        String name
) {
    public UserInfoResponse(User user) {
        this(user.getName());
    }
}
