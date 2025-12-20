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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MypageRepository mypageRepository;
    private final PracticeLogRepository practiceLogRepository;
    private final UserRepository userRepository;

    @Transactional
    public Mypage getMonthlySummary(Long userId, String yearMonth) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        List<PracticeLog> logs =
                practiceLogRepository.findByUserIdAndYearMonth(userId, yearMonth);

        int totalCount = logs.size();
        
        // 평균 정확도 계산 (각 로그의 accuracy 평균)
        int avgAccuracy = (int) logs.stream()
                .mapToInt(PracticeLog::getAccuracy)
                .average()
                .orElse(0);

        int avgDurationSec = (int) logs.stream()
                .mapToInt(PracticeLog::getDurationSec)
                .average()
                .orElse(0);
        
        Difficulty difficulty = recommendDifficulty(totalCount, avgAccuracy, avgDurationSec);

        // 이미 존재하는 Mypage가 있는지 확인
        Optional<Mypage> existingMypage = mypageRepository.findByUser_IdAndYearMonth(userId, yearMonth);

        if (existingMypage.isPresent()) {
            // 있으면 업데이트
            Mypage mypage = existingMypage.get();
            mypage.update(totalCount, avgAccuracy, avgDurationSec, difficulty);
            return mypage;
        } else {
            // 없으면 새로 생성
            Mypage newMypage = Mypage.builder()
                    .user(user)
                    .yearMonth(yearMonth)
                    .practiceCount(totalCount)
                    .avgSuccessRate(avgAccuracy)
                    .avgDurationSec(avgDurationSec)
                    .recommendDifficulty(difficulty)
                    .build();
            return mypageRepository.save(newMypage);
        }
    }

    private Difficulty recommendDifficulty(
            int practiceCount,
            int avgSuccessRate,
            int avgDurationSec
    ) {
        if (practiceCount <= 5) {
            // 이거 추후 수정해야함
            return Difficulty.초보;
        }

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
