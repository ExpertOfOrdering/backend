package com.expertofordering.service;

import com.expertofordering.entity.*;
import com.expertofordering.repository.PracticeLogRepository;
import com.expertofordering.repository.UserDifficultyRepository;
import com.expertofordering.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 각 테스트가 끝난 후 데이터베이스 변경 사항을 자동으로 롤백합니다.
class MypageServiceTest {

    @Autowired
    private MypageService mypageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDifficultyRepository userDifficultyRepository;

    @Autowired
    private PracticeLogRepository practiceLogRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // 모든 테스트가 실행되기 전에, 테스트용 사용자를 미리 생성하고 저장합니다.
        testUser = User.builder()
                .name("테스트유저")
                .username("testuser_mypage")
                .passwordHash("$2a$10$dummyPasswordHashValue12345") // 실제 해시값 예시
                .role(Role.USER)
                .build();
        userRepository.save(testUser);
    }

    @Test
    @DisplayName("초기 난이도 설정이 없고 로그가 5개 초과일 때, 마이페이지 조회 및 난이도 갱신 테스트")
    void getMonthlySummary_WhenNoInitialDifficultyAndLogsOverFive() {
        // Given (준비):
        // 1. UserDifficulty 데이터가 없는 상태입니다. (setUp에서 생성 안 함)
        // 2. 11월의 연습 로그를 6개 생성하고 저장합니다.
        practiceLogRepository.save(PracticeLog.builder().user(testUser).month(11).DurationSec(60).accuracy(80).build());
        practiceLogRepository.save(PracticeLog.builder().user(testUser).month(11).DurationSec(70).accuracy(90).build());
        practiceLogRepository.save(PracticeLog.builder().user(testUser).month(11).DurationSec(80).accuracy(100).build());
        practiceLogRepository.save(PracticeLog.builder().user(testUser).month(11).DurationSec(50).accuracy(70).build());
        practiceLogRepository.save(PracticeLog.builder().user(testUser).month(11).DurationSec(90).accuracy(85).build());
        practiceLogRepository.save(PracticeLog.builder().user(testUser).month(11).DurationSec(100).accuracy(95).build());

        // When (실행):
        // 해당 사용자의 11월 마이페이지 정보를 조회합니다.
        Mypage mypage = mypageService.getMonthlySummary(testUser.getUsername(), 11);

        // Then (검증):
        // 1. Mypage 객체가 정상적으로 반환되었는지 확인합니다.
        assertNotNull(mypage, "Mypage 객체는 null이 아니어야 합니다.");
        assertEquals(6, mypage.getPracticeCount(), "연습 횟수는 6회여야 합니다.");
        assertEquals(86, mypage.getAvgSuccessRate(), "평균 정확도는 86이어야 합니다."); // (80+90+100+70+85+95)/6 = 86.6 -> int 변환으로 86

        // 2. UserDifficulty 테이블에 데이터가 새로 생성되었거나 업데이트되었는지 확인합니다.
        var userDifficultyOpt = userDifficultyRepository.findById(testUser.getId());
        assertTrue(userDifficultyOpt.isPresent(), "UserDifficulty 데이터가 생성되어야 합니다.");

        // 3. 새로 계산된 추천 난이도가 올바르게 반영되었는지 확인합니다.
        // (86점, 75초) -> 성공점수 3점, 시간점수 2점 -> 총 5점 -> 중수
        assertEquals(Difficulty.중수, userDifficultyOpt.get().getCurrentRecommendedDifficulty(), "추천 난이도는 '중수'여야 합니다.");
    }
}
