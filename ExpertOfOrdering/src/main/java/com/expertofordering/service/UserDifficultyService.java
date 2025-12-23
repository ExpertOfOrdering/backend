package com.expertofordering.service;

import com.expertofordering.entity.Difficulty;
import com.expertofordering.entity.User;
import com.expertofordering.entity.UserDifficulty;
import com.expertofordering.repository.UserDifficultyRepository;
import com.expertofordering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDifficultyService {

    private final UserDifficultyRepository userDifficultyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void setInitialDifficulty(String username, int score) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));

        Difficulty initialDifficulty = calculateDifficultyFromScore(score);

        UserDifficulty userDifficulty = UserDifficulty.builder()
                .user(user)
                .currentRecommendedDifficulty(initialDifficulty)
                .build();

        userDifficultyRepository.save(userDifficulty);
    }

    @Transactional
    public void updateCurrentDifficulty(Long userId, Difficulty newDifficulty) {
        UserDifficulty userDifficulty = userDifficultyRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserDifficulty not found for user id: " + userId));
        userDifficulty.updateDifficulty(newDifficulty);
    }

    @Transactional(readOnly = true)
    public UserDifficulty getCurrentDifficulty(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        return userDifficultyRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserDifficulty not found for user: " + username));
    }

    private Difficulty calculateDifficultyFromScore(int score) {
        if (score <= 3) {
            return Difficulty.초보;
        } else if (score <= 5) {
            return Difficulty.중수;
        } else {
            return Difficulty.달인;
        }
    }
}
