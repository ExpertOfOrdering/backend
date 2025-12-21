package com.expertofordering.controller;

import com.expertofordering.dto.PracticeLogRequest;
import com.expertofordering.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Practice Log", description = "Endpoints for managing practice logs")
@RestController
@RequestMapping("/api/practice-logs")
@RequiredArgsConstructor
public class PracticeLogController {

    private final MypageService mypageService;

    @Operation(summary = "Save a new practice log", description = "Saves the result of a single practice session.")
    @PostMapping
    public ResponseEntity<Void> savePracticeLog(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PracticeLogRequest request
    ) {
        String username = userDetails.getUsername();
        mypageService.savePracticeLog(username, request);
        return ResponseEntity.ok().build();
    }
}
