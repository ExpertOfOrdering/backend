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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Practice Log", description = "Endpoints for managing practice logs")
@RestController
@RequestMapping("/api/practice-logs")
@RequiredArgsConstructor
public class PracticeLogController {

    private final MypageService mypageService;

    @Operation(
            summary = "Save a new practice log",
            description = "Saves the result of a single practice session.",
            requestBody = @RequestBody(
                    description = "Details of the practice session to be saved.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PracticeLogRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "userId": 1,
                                        "month": 11,
                                        "durationSec": 120,
                                        "accuracy": 88
                                    }
                                    """)
                    )
            )
    )
    @PostMapping
    public ResponseEntity<Void> savePracticeLog(
            @Valid @org.springframework.web.bind.annotation.RequestBody PracticeLogRequest request
    ) {
        mypageService.savePracticeLog(request);
        return ResponseEntity.ok().build();
    }
}
