package com.citas.citas_medicas_backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.citas.citas_medicas_backend.dto.UserRequest;
import com.citas.citas_medicas_backend.dto.UserResponse;
import com.citas.citas_medicas_backend.model.Role;
import com.citas.citas_medicas_backend.model.User;
import com.citas.citas_medicas_backend.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Listar todos
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Obtener por id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(toResponse(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear usuario
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
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
        user.setEnabled(request.getEnabled() != null ? request.getEnabled() : true);

        User saved = userRepository.save(user);
        return ResponseEntity.ok(toResponse(saved));
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @RequestBody UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (request.getEmail() != null) user.setEmail(request.getEmail());
                    if (request.getPassword() != null) user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                    if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
                    if (request.getLastName() != null) user.setLastName(request.getLastName());
                    if (request.getPhotoUrl() != null) user.setPhotoUrl(request.getPhotoUrl());
                    if (request.getRole() != null) user.setRole(request.getRole());
                    if (request.getEnabled() != null) user.setEnabled(request.getEnabled());
                    User updated = userRepository.save(user);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

// Borrado lógico (desactivar)
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> delete(@PathVariable Long id) {
    return userRepository.findById(id)
            .map(user -> {
                user.setEnabled(false);
                userRepository.save(user);
                return ResponseEntity.noContent().build();
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
}


    // Obtener perfil del usuario autenticado
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(toResponse(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar perfil del usuario autenticado
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateProfile(Authentication authentication, @RequestBody UserRequest request) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .map(user -> {
                    if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
                    if (request.getLastName() != null) user.setLastName(request.getLastName());
                    if (request.getPhotoUrl() != null) user.setPhotoUrl(request.getPhotoUrl());
                    // No permitir cambiar email, password, role desde perfil
                    User updated = userRepository.save(user);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
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
