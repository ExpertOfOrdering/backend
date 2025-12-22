package com.expertofordering.controller;

import com.expertofordering.dto.InitialDifficultyRequest;
import com.expertofordering.dto.UserDifficultyResponse;
import com.expertofordering.entity.UserDifficulty;
import com.expertofordering.service.UserDifficultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Difficulty", description = "Endpoints for managing user's recommended difficulty")
@RestController
@RequestMapping("/api/difficulty")
@RequiredArgsConstructor
public class UserDifficultyController {

    private final UserDifficultyService userDifficultyService;

    @Operation(summary = "Set Initial Difficulty", description = "Sets the user's initial recommended difficulty based on a score. (Requires JWT Token)")
    @PostMapping("/initial")
    public ResponseEntity<Void> setInitialDifficulty(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody InitialDifficultyRequest request
    ) {
        userDifficultyService.setInitialDifficulty(userDetails.getUsername(), request.getScore());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get Current Difficulty", description = "Gets the user's current recommended difficulty. (Requires JWT Token)")
    @GetMapping("/current")
    public ResponseEntity<UserDifficultyResponse> getCurrentDifficulty(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserDifficulty userDifficulty = userDifficultyService.getCurrentDifficulty(userDetails.getUsername());
        return ResponseEntity.ok(new UserDifficultyResponse(userDifficulty));
    }
}
