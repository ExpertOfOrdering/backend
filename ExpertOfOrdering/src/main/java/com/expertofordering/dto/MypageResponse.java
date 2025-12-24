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

    @Schema(description = "Month of the summary (1-12)", example = "11")
    private final String month;

    @Schema(description = "Total practice count in the month", example = "15회")
    private final String practiceCount;

    @Schema(description = "Average success rate (%)", example = "88%")
    private final String avgSuccessRate;

    @Schema(description = "Average duration", example = "1분 30초")
    private final String avgDuration;

    @Schema(description = "Recommended difficulty for the next practice", example = "중수")
    private final Difficulty recommendDifficulty;

    public MypageResponse(Mypage mypage) {
        this.id = mypage.getId();
        this.month = mypage.getMonth() + "월";
        this.practiceCount = mypage.getPracticeCount() + "회";
        this.avgSuccessRate = mypage.getAvgSuccessRate() + "%";
        
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
