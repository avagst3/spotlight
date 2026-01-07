package com.spotlight.back.spotlight.services;

import com.spotlight.back.spotlight.managers.JwtManager;
import com.spotlight.back.spotlight.models.dtos.AuthenticationRequest;
import com.spotlight.back.spotlight.models.dtos.AuthenticationResponse;
import com.spotlight.back.spotlight.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtManager jwtManager;

    @Transactional
    public AuthenticationResponse authenticateUser(AuthenticationRequest dto) {
        return userRepository.findByUsername(dto.getUsername())
                .filter(user -> encoder.matches(dto.getPassword(), user.getPassword()))
                .map(user -> AuthenticationResponse.builder()
                        .userId(user.getId())
                        .username(user.getName())
                        .token(jwtManager.generateToken(user))
                        .expiresIn(jwtManager.getExpirationTime())
                        .build())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
