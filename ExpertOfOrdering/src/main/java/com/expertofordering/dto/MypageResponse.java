package com.expertofordering.dto;

import com.expertofordering.entity.Difficulty;
import com.expertofordering.entity.Mypage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "Mypage monthly summary response")
@Getter
public class MypageResponse {

    @Schema(description = "Mypage data ID", example = "1")
    private final Long id;

    @Schema(description = "Year and month of the summary", example = "2025-10")
    private final String yearMonth;

    @Schema(description = "Total practice count in the month", example = "15회")
    private final String practiceCount;

    @Schema(description = "Average success rate (%)", example = "85%")
    private final String avgSuccessRate;

    @Schema(description = "Average duration", example = "2분 0초")
    private final String avgDuration;

    @Schema(description = "Recommended difficulty for the next practice", example = "중수")
    private final Difficulty recommendDifficulty;

    public MypageResponse(Mypage mypage) {
        this.id = mypage.getId();
        this.yearMonth = mypage.getYearMonth();
        
        // "회" 붙이기
        this.practiceCount = mypage.getPracticeCount() + "회";
        
        // "%" 붙이기 (int -> String 변환)
        this.avgSuccessRate = mypage.getAvgSuccessRate() + "%";
        
        // 초 -> "X분 Y초" 또는 "Y초" 변환
        int totalSec = mypage.getAvgDurationSec();
        int min = totalSec / 60;
        int sec = totalSec % 60;
        
        if (min > 0) {
            this.avgDuration = min + "분 " + sec + "초";
        } else {
            this.avgDuration = sec + "초";
        }

        this.recommendDifficulty = mypage.getRecommendDifficulty();
    }
}
