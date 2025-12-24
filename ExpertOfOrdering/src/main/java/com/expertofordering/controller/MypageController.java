package com.expertofordering.controller;

import com.expertofordering.dto.MypageResponse;
import com.expertofordering.entity.Mypage;
import com.expertofordering.service.MypageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Mypage", description = "Mypage data endpoints")
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MypageController {

    private final MypageService mypageService;

    @Operation(summary = "Get Monthly Summary", description = "Retrieve the monthly practice summary for the logged-in user. (Calculates on the fly)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MypageResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "month": "11월",
                                        "practiceCount": "15회",
                                        "avgSuccessRate": "88%",
                                        "avgDuration": "1분 30초",
                                        "recommendDifficulty": "중수"
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping
    public ResponseEntity<MypageResponse> getMonthlySummary(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int month
    ) {
        String username = userDetails.getUsername();
        Mypage mypage = mypageService.getMonthlySummary(username, month);
        return ResponseEntity.ok(new MypageResponse(mypage));
    }
}
