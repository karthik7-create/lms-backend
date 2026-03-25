package com.lms.lms.Controller;

package com.leavemgmt.controller;

import com.lms.lms.dto.LoginRequest;
import com.lms.lms.dto.LoginResponse;
import com.lms.lms.dto.RegisterRequest;
import com.lms.lms.entity.User;
import com.lms.lms.exception.ResourceNotFoundException;
import com.lms.lms.repository.UserRepository;
import com.lms.lms.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        boolean isManager = user.getManager() == null;
        return ResponseEntity.ok(new LoginResponse(token, user.getId(),
                user.getFullName(), user.getEmail(), isManager ? "MANAGER" : "EMPLOYEE"));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        User manager = null;
        if (request.managerId() != null) {
            manager = userRepository.findById(request.managerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
        }
        User user = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .manager(manager)
                .isActive(true)
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
}