package com.expertofordering.controller;

import com.expertofordering.dto.MypageResponse;
import com.expertofordering.service.MonthlySummaryCalculator;
import com.expertofordering.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Mypage", description = "Mypage data endpoints")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;
    private final MonthlySummaryCalculator monthlySummaryCalculator;

    @Operation(summary = "Calculate Monthly Summary", description = "Calculate and save the monthly practice summary for a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully calculated"),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or yearMonth format"),
            @ApiResponse(responseCode = "404", description = "Mypage data not found")
    })
    @PostMapping("/calculate")
    public ResponseEntity<Void> calculateMonthlySummary(@RequestParam Long userId, @RequestParam String yearMonth) {
        monthlySummaryCalculator.calculator(userId, yearMonth);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get Monthly Summary", description = "Retrieve the monthly practice summary for a user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Mypage data not found")
    })
    @GetMapping
    public ResponseEntity<MypageResponse> getMonthlySummary(@RequestParam Long userId, @RequestParam String yearMonth) {
        Optional<MypageResponse> mypageDto = mypageService.getMonthlySummary(userId, yearMonth);
        return mypageDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
