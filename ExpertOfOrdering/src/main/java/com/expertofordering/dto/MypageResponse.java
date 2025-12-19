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

    @Schema(description = "Total practice count in the month", example = "15")
    private final int practiceCount;

    @Schema(description = "Average success rate (%)", example = "85")
    private final int avgSuccessRate;

    @Schema(description = "Average duration in seconds", example = "120")
    private final int avgDurationSec;

    @Schema(description = "Recommended difficulty for the next practice", example = "중수")
    private final Difficulty recommendDifficulty;

    public MypageResponse(Mypage mypage) {
        this.id = mypage.getId();
        this.yearMonth = mypage.getYearMonth();
        this.practiceCount = mypage.getPracticeCount();
        this.avgSuccessRate = mypage.getAvgSuccessRate();
        this.avgDurationSec = mypage.getAvgDurationSec();
        this.recommendDifficulty = mypage.getRecommendDifficulty();
    }
}
