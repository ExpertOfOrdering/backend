package com.expertofordering.service;

import com.expertofordering.entity.Difficulty;
import com.expertofordering.entity.User;
import com.expertofordering.entity.UserDifficulty;
import com.expertofordering.repository.UserDifficultyRepository;
import com.expertofordering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDifficultyService {

    private final UserDifficultyRepository userDifficultyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void setInitialDifficulty(Long userId, int score) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Difficulty initialDifficulty = calculateDifficultyFromScore(score);

        UserDifficulty userDifficulty = UserDifficulty.builder()
                .user(user)
                .currentRecommendedDifficulty(initialDifficulty)
                .build();

        userDifficultyRepository.save(userDifficulty);
    }

    @Transactional
    public void updateCurrentDifficulty(Long userId, Difficulty newDifficulty) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Optional<UserDifficulty> existingDifficulty = userDifficultyRepository.findById(userId);

        if (existingDifficulty.isPresent()) {
            existingDifficulty.get().updateDifficulty(newDifficulty);
        } else {
            UserDifficulty newUserDifficulty = UserDifficulty.builder()
                    .user(user)
                    .currentRecommendedDifficulty(newDifficulty)
                    .build();
            userDifficultyRepository.save(newUserDifficulty);
        }
    }

    @Transactional(readOnly = true)
    public UserDifficulty getCurrentDifficulty(Long userId) {
        return userDifficultyRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserDifficulty not found for user id: " + userId));
    }

    private Difficulty calculateDifficultyFromScore(int score) {
        if (score <= 4) {
            return Difficulty.초보;
        } else if (score <= 5) {
            return Difficulty.중수;
        } else {
            return Difficulty.달인;
        }
    }
}
