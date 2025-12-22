package com.expertofordering.controller;

import com.expertofordering.dto.PracticeLogRequest;
import com.expertofordering.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Practice Log", description = "연습 로그 API")
@RestController
@RequestMapping("/api/practice-logs")
@RequiredArgsConstructor
public class PracticeLogController {

    private final MypageService mypageService;

    @Operation(
            summary = "Save a new practice log",
            description = "시뮬레이션 기록을 저장합니다. <br><br> **Note:** 헤더에 JWT토큰  'Authorization: Bearer {token}' 이 형태로 넣어야 합니다!",
            requestBody = @RequestBody(
                    description = "Details of the practice session to be saved.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PracticeLogRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "yearMonth": "2023-11",
                                        "durationSec": 120,
                                        "accuracy": 88
                                    }
                                    """)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Void> savePracticeLog(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @org.springframework.web.bind.annotation.RequestBody PracticeLogRequest request
    ) {
        String username = userDetails.getUsername();
        mypageService.savePracticeLog(username, request);
        return ResponseEntity.ok().build();
    }
}
