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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "Endpoints for user information")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get Current User's Name",
            description = "현재 로그인된 사용자의 이름을 조회합니다. <br><br> **Note:** 헤더에 JWT 토큰이 필요합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "사용자 이름 조회 성공",
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
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(new UserInfoResponse(user));
    }
}
