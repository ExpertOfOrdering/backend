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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Practice Log", description = "연습 로그 API")
@RestController
@RequestMapping("/api/practice-logs")
@RequiredArgsConstructor
public class PracticeLogController {

    private final MypageService mypageService;

    @Operation(
            summary = "Save a new practice log",
            description = "시뮬레이션 기록을 저장합니다.",
            requestBody = @RequestBody(
                    description = "Details of the practice session to be saved.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PracticeLogRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "month": "2023-11",
                                        "durationSec": 120,
                                        "accuracy": 88
                                    }
                                    """)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Void> savePracticeLog(
            @RequestParam String username,
            @Valid @org.springframework.web.bind.annotation.RequestBody PracticeLogRequest request
    ) {
        mypageService.savePracticeLog(username, request);
        return ResponseEntity.ok().build();
    }
}
