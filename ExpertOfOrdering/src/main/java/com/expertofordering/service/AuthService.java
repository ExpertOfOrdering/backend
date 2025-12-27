package com.expertofordering.service;

import com.expertofordering.dto.AuthResponse;
import com.expertofordering.dto.LoginRequest;
import com.expertofordering.dto.SignupRequest;
import com.expertofordering.entity.Role;
import com.expertofordering.entity.User;
import com.expertofordering.exception.ApiException;
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
        String name = req.name().trim();

        System.out.println("[AUTH][SIGNUP][REQ] username=" + username + ", name=[" + name + "]");

        if (userRepository.existsByUsername(username)) {
            System.out.println("[AUTH][SIGNUP][FAIL] username already exists: " + username);
            throw ApiException.conflict("USERNAME_ALREADY_EXISTS", "Username already exists");
        }

        User user = User.builder()
                .name(name)
                .username(username)
                // 해시 안 함: DB 컬럼명이 passwordHash여도 여기에 평문 저장
                .passwordHash(req.password())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        System.out.println("[AUTH][SIGNUP][OK] id=" + user.getId()
                + ", username=" + user.getUsername()
                + ", name=[" + user.getName() + "]");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String username = req.username().trim();

        System.out.println("[AUTH][LOGIN][REQ] username=" + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("[AUTH][LOGIN][FAIL] user not found: " + username);
                    return ApiException.unauthorized("INVALID_CREDENTIALS", "Invalid credentials");
                });

        if (user.getPasswordHash() == null || !user.getPasswordHash().equals(req.password())) {
            System.out.println("[AUTH][LOGIN][FAIL] password mismatch for username=" + username);
            throw ApiException.unauthorized("INVALID_CREDENTIALS", "Invalid credentials");
        }

        System.out.println("[AUTH][LOGIN][OK] id=" + user.getId()
                + ", username=" + user.getUsername()
                + ", name=[" + user.getName() + "]");

        return new AuthResponse(user.getId(), user.getName(), user.getUsername());
    }
}
