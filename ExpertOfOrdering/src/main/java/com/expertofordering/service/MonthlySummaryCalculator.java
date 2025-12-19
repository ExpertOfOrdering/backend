package com.expertofordering.service;

import com.expertofordering.entity.Difficulty;
import com.expertofordering.entity.Mypage;
import com.expertofordering.entity.PracticeLog;
import com.expertofordering.entity.User;
import com.expertofordering.repository.MypageRepository;
import com.expertofordering.repository.PracticeLogRepository;
import com.expertofordering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlySummaryCalculator {

    private final MypageRepository mypageRepository;
    private final PracticeLogRepository practiceLogRepository;
    private final UserRepository userRepository;

    public void calculator(Long userId, String yearMonth) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        List<PracticeLog> logs =
                practiceLogRepository.findByUserIdAndYearMonth(userId, yearMonth);

        int totalCount = logs.size();
        int successCount = (int) logs.stream()
                .filter(PracticeLog::isSuccess)
                .count();
        int avgDurationSec = (int) logs.stream()
                .mapToInt(PracticeLog::getDurationSec)
                .average()
                .orElse(0);
        int successRate = totalCount == 0
                ? 0
                : (successCount * 100) / totalCount;

        Mypage mypage = Mypage.builder()
                .user(user)
                .yearMonth(yearMonth)
                .practiceCount(totalCount)
                .avgSuccessRate(successRate)
                .avgDurationSec(avgDurationSec)
                .recommendDifficulty(
                        recommendDifficulty(totalCount, successRate, avgDurationSec)
                )
                .build();
        mypageRepository.save(mypage);
    }

    private Difficulty recommendDifficulty(
            int practiceCount,
            int avgSuccessRate,
            int avgDurationSec
    ) {
        if (practiceCount <= 5) {
            return Difficulty.BEGINNER;
        }

        int successScore = calculateSuccessScore(avgSuccessRate);
        int durationScore = calculateDurationScore(avgDurationSec);

        int totalScore = successScore + durationScore;

        if (totalScore <= 3) {
            return Difficulty.BEGINNER;
        } else if (totalScore <= 5) {
            return Difficulty.INTERMEDIATE;
        } else {
            return Difficulty.EXPERT;
        }
    }

    private int calculateSuccessScore(int avgSuccessRate) {
        if (avgSuccessRate < 50) {
            return 1;
        } else if (avgSuccessRate < 80) {
            return 2;
        } else {
            return 3;
        }

    }

    private int calculateDurationScore(int avgDurationSec) {
        if (avgDurationSec >= 180) {
            return 1;
        } else if (avgDurationSec >= 60) {
            return 2;
        } else {
            return 3;
        }

    }

}
