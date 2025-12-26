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
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Difficulty", description = "사용자의 추천 난이도를 관리하는 API")
@RestController
@RequestMapping("/api/difficulty")
@RequiredArgsConstructor
public class UserDifficultyController {

    private final UserDifficultyService userDifficultyService;

    @Operation(
            summary = "초기 추천 난이도 설정",
            description = "회원가입 후, 사용자의 초기 테스트 점수를 기반으로 추천 난이도를 설정합니다. (최초 1회 호출)",
            requestBody = @RequestBody(
                    description = "초기 테스트 결과로 계산된 점수와 사용자 ID",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InitialDifficultyRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "userId": 1,
                                        "score": 7
                                    }
                                    """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Initial difficulty set successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PostMapping("/initial")
    public ResponseEntity<Void> setInitialDifficulty(
            @Valid @org.springframework.web.bind.annotation.RequestBody InitialDifficultyRequest request
    ) {
        userDifficultyService.setInitialDifficulty(request.getUserId(), request.getScore());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "현재 추천 난이도 조회",
            description = "사용자 ID를 기준으로 현재 추천 난이도를 조회합니다. (게임 난이도 선택 화면 등에서 사용)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved current difficulty",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDifficultyResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "currentRecommendedDifficulty": "중수"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "User or difficulty info not found", content = @Content)
    })
    @GetMapping("/current")
    public ResponseEntity<UserDifficultyResponse> getCurrentDifficulty(
            @RequestParam Long userId
    ) {
        UserDifficulty userDifficulty = userDifficultyService.getCurrentDifficulty(userId);
        return ResponseEntity.ok(new UserDifficultyResponse(userDifficulty));
    }
}
