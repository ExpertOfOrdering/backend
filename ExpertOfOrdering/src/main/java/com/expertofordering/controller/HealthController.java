package com.expertofordering.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;

import java.util.Map;

@Tag(name = "Health", description = "서버 헬스체크 엔드포인트")
@RestController
public class HealthController {

    @Operation(
            summary = "Health check",
            description = "서버 문제 없을 tl 200 OK return"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Server is healthy")
    })
    @GetMapping(value = "/health", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
