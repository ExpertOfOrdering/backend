package com.expertofordering.service;

import com.expertofordering.dto.AuthResponse;
import com.expertofordering.dto.LoginRequest;
import com.expertofordering.dto.SignupRequest;
import com.expertofordering.entity.Role;
import com.expertofordering.entity.User;
import com.expertofordering.repository.UserRepository;
import com.expertofordering.security.jwt.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
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
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String username = req.username().trim();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String accessToken = tokenProvider.createAccessToken(user.getUsername());
        return new AuthResponse(accessToken);
    }
}
