package com.citas.citas_medicas_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citas.citas_medicas_backend.dto.AuthRequest;
import com.citas.citas_medicas_backend.dto.AuthResponse;
import com.citas.citas_medicas_backend.dto.LoginRequest;
import com.citas.citas_medicas_backend.dto.RefreshRequest;
import com.citas.citas_medicas_backend.dto.UserResponse;
import com.citas.citas_medicas_backend.model.Role;
import com.citas.citas_medicas_backend.model.User;
import com.citas.citas_medicas_backend.repository.UserRepository;
import com.citas.citas_medicas_backend.security.JwtService;

import jakarta.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody AuthRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhotoUrl(request.getPhotoUrl());
        user.setRole(request.getRole() != null ? request.getRole() : Role.PACIENTE);
        user.setEnabled(true);

        User saved = userRepository.save(user);
        return ResponseEntity.ok(toResponse(saved));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).build();
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, refreshToken, user.getEmail(), user.getRole().name()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        String email = jwtService.extractEmail(refreshToken);

        if (email != null && jwtService.isRefreshTokenValid(refreshToken, email)) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String newToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
                String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());
                return ResponseEntity.ok(new AuthResponse(newToken, newRefreshToken, user.getEmail(), user.getRole().name()));
            }
        }
        return ResponseEntity.status(401).build();
    }

    private UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhotoUrl(user.getPhotoUrl());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}

