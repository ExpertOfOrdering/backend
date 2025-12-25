package com.expertofordering.service;

import com.expertofordering.dto.AuthResponse;
import com.expertofordering.dto.LoginRequest;
import com.expertofordering.dto.SignupRequest;
import com.expertofordering.entity.Role;
import com.expertofordering.entity.User;
import com.expertofordering.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void signup(SignupRequest req) {
        String username = req.username().trim();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = User.builder()
                .name(req.name().trim())
                .username(username)
                // 해시 안 함: DB 컬럼명이 passwordHash여도 여기에 평문 저장
                .passwordHash(req.password())
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String username = req.username().trim();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // 해시 비교 없음: 문자열 그대로 비교
        if (user.getPasswordHash() == null || !user.getPasswordHash().equals(req.password())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new AuthResponse(user.getId(), user.getName(), user.getUsername());
    }
}
