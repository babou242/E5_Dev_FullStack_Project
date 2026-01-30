package com.example.TP3_Pinia.controller;


import com.example.TP3_Pinia.dto.AuthResponse;
import com.example.TP3_Pinia.dto.LoginRequest;
import com.example.TP3_Pinia.dto.UserResponse;
import com.example.TP3_Pinia.model.User;
import com.example.TP3_Pinia.security.JwtUtils;
import com.example.TP3_Pinia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authentifier l’utilisateur
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Generer le token
            String token = jwtUtils.generateToken(request.getUsername());
            User user = userService.findByUsername(request.getUsername());

            return ResponseEntity.ok(new AuthResponse(
                    token,
                    user.getUsername(),
                    user.getRole().name()
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Identifiants invalides"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(new UserResponse(
                user.getUsername(),
                user.getRole().name()
        ));
    }
}
