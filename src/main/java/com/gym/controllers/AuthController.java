package com.gym.controllers;

import com.gym.dto.requests.ChangePasswordRequest;
import com.gym.dto.requests.LoginRequest;
import com.gym.dto.responses.JwtResponse;
import com.gym.services.AuthService;
import com.gym.services.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//REST Controller for auth
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API")
public class AuthController {

    private final AuthService authService;
    private final TraineeService traineeService;

    public AuthController(AuthService authService, TraineeService traineeService) {
        this.authService = authService;
        this.traineeService = traineeService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Returns JWT token"
    )
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {

        JwtResponse response = authService.authenticate(
                request.username(),
                request.password()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(
            summary = "User logout",
            description = "User logs out, no JWT token"
    )
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        authService.logout(token);

        return ResponseEntity.ok("Logged out successfully!");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.authenticate(request.getUsername(), request.getOldPassword());

        traineeService.changePassword(
                request.getUsername(),
                request.getNewPassword()
        );

        return ResponseEntity.ok("Password changed!");
    }
}