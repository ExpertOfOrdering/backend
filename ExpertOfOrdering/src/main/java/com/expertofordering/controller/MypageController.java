package com.expertofordering.controller;

import com.expertofordering.dto.MypageResponse;
import com.expertofordering.entity.Mypage;
import com.expertofordering.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mypage", description = "Mypage data endpoints")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @Operation(summary = "Get Monthly Summary", description = "Retrieve the monthly practice summary for a user. (Calculates on the fly)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    public ResponseEntity<MypageResponse> getMonthlySummary(@RequestParam Long userId, @RequestParam String yearMonth) {
        // GET 요청 시 항상 최신 상태로 계산(또는 업데이트) 후 결과를 반환
        Mypage mypage = mypageService.getMonthlySummary(userId, yearMonth);
        return ResponseEntity.ok(new MypageResponse(mypage));
    }
}
