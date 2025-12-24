package com.expertofordering.service;

import com.expertofordering.dto.PracticeLogRequest;
import com.expertofordering.entity.Difficulty;
import com.expertofordering.entity.Mypage;
import com.expertofordering.entity.PracticeLog;
import com.expertofordering.entity.User;
import com.expertofordering.repository.MypageRepository;
import com.expertofordering.repository.PracticeLogRepository;
import com.expertofordering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MypageRepository mypageRepository;
    private final PracticeLogRepository practiceLogRepository;
    private final UserRepository userRepository;
    private final UserDifficultyService userDifficultyService;

    @Transactional
    public void savePracticeLog(String username, PracticeLogRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        PracticeLog newLog = PracticeLog.builder()
                .user(user)
                .month(request.getMonth())
                .DurationSec(request.getDurationSec())
                .accuracy(request.getAccuracy())
                .build();

        practiceLogRepository.save(newLog);
    }

    @Transactional
    public Mypage getMonthlySummary(String username, Integer month) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        List<PracticeLog> logs =
                practiceLogRepository.findByUserIdAndMonth(user.getId(), month);

        int totalCount = logs.size();
        
        int avgAccuracy = (int) logs.stream()
                .mapToInt(PracticeLog::getAccuracy)
                .average()
                .orElse(0);

        int avgDurationSec = (int) logs.stream()
                .mapToInt(PracticeLog::getDurationSec)
                .average()
                .orElse(0);
        
        Difficulty monthlyRecommendDifficulty;

        if (totalCount <= 5) {
            monthlyRecommendDifficulty = userDifficultyService.getCurrentDifficulty(username).getCurrentRecommendedDifficulty();
        } else {
            monthlyRecommendDifficulty = recommendDifficulty(avgAccuracy, avgDurationSec);
            userDifficultyService.updateCurrentDifficulty(user.getId(), monthlyRecommendDifficulty);
        }

        Optional<Mypage> existingMypage = mypageRepository.findByUser_IdAndMonth(user.getId(), month);

        if (existingMypage.isPresent()) {
            Mypage mypage = existingMypage.get();
            mypage.update(totalCount, avgAccuracy, avgDurationSec, monthlyRecommendDifficulty);
            return mypage;
        } else {
            Mypage newMypage = Mypage.builder()
                    .user(user)
                    .month(month)
                    .practiceCount(totalCount)
                    .avgSuccessRate(avgAccuracy)
                    .avgDurationSec(avgDurationSec)
                    .recommendDifficulty(monthlyRecommendDifficulty)
                    .build();
            return mypageRepository.save(newMypage);
        }
    }

    private Difficulty recommendDifficulty(int avgSuccessRate, int avgDurationSec) {
        int successScore = calculateSuccessScore(avgSuccessRate);
        int durationScore = calculateDurationScore(avgDurationSec);

        int totalScore = successScore + durationScore;

        if (totalScore <= 3) {
            return Difficulty.초보;
        } else if (totalScore <= 5) {
            return Difficulty.중수;
        } else {
            return Difficulty.달인;
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
