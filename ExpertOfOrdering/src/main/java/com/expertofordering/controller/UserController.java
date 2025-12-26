package com.expertofordering.controller;

import com.expertofordering.dto.UserInfoResponse;
import com.expertofordering.entity.User;
import com.expertofordering.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "Endpoints for user information")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get User's Name by ID",
            description = "사용자 ID를 기준으로 사용자의 표시 이름(name)을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved user name",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserInfoResponse.class),
                    examples = @ExampleObject(value = """
                            {
                                "name": "홍길동"
                            }
                            """)
            )
    )
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponse> getUserNameById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(new UserInfoResponse(user));
    }
}
