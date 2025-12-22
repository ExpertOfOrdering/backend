package com.expertofordering.controller;

import com.expertofordering.dto.InitialDifficultyRequest;
import com.expertofordering.dto.UserDifficultyResponse;
import com.expertofordering.entity.UserDifficulty;
import com.expertofordering.service.UserDifficultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Difficulty", description = "사용자의 추천 난이도를 관리하는 API")
@RestController
@RequestMapping("/api/difficulty")
@RequiredArgsConstructor
public class UserDifficultyController {

    private final UserDifficultyService userDifficultyService;

    @Operation(
            summary = "초기 추천 난이도 설정",
            description = "회원가입 후, 사용자의 초기 테스트 점수를 기반으로 추천 난이도를 설정합니다. (최초 1회 호출) <br><br> **Note:** 헤더에 JWT 토큰이 필요합니다.",
            requestBody = @RequestBody(
                    description = "초기 테스트 결과로 계산된 점수",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InitialDifficultyRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "score": 7
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "초기 난이도 설정 성공"),
            @ApiResponse(responseCode = "400", description = "요청 점수 값(score)이 없음", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content)
    })
    @PostMapping("/initial")
    public ResponseEntity<Void> setInitialDifficulty(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @org.springframework.web.bind.annotation.RequestBody InitialDifficultyRequest request
    ) {
        userDifficultyService.setInitialDifficulty(userDetails.getUsername(), request.getScore());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "현재 추천 난이도 조회",
            description = "사용자의 현재 추천 난이도를 조회합니다. (게임 난이도 선택 화면 등에서 사용) <br><br> **Note:** 헤더에 JWT 토큰이 필요합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "현재 난이도 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDifficultyResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "currentRecommendedDifficulty": "중수"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자 또는 난이도 정보 없음", content = @Content)
    })
    @GetMapping("/current")
    public ResponseEntity<UserDifficultyResponse> getCurrentDifficulty(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDifficulty userDifficulty = userDifficultyService.getCurrentDifficulty(userDetails.getUsername());
        return ResponseEntity.ok(new UserDifficultyResponse(userDifficulty));
    }
}
