package com.expertofordering.dto;

import com.expertofordering.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Response with basic user information")
public class UserInfoResponse {

    @Schema(description = "User's display name", example = "홍길동")
    private final String name;

    public UserInfoResponse(User user) {
        this.name = user.getName();
    }
}
