package com.gym.controllers;

import com.gym.dto.requests.ChangePasswordRequest;
import com.gym.dto.requests.LoginRequest;
import com.gym.services.AuthService;
import com.gym.services.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//REST Controller for auth
@RestController
@RequestMapping("api/v1/auth")
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
            description = "User logs in",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User logged successfully")
            }
    )
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        boolean result = authService.authenticate(
                request.getUsername(),
                request.getPassword()
        );

        if (!result) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }

        return ResponseEntity.ok("Login successful!");
    }

    @PutMapping("/change-password")
    @Operation(
            summary = "Change password",
            description = "Changes user password",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Password changed successfully")
            }
    )
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        boolean authenticated = authService.authenticate(changePasswordRequest.getUsername(), changePasswordRequest.getOldPassword());

        if(!authenticated) {
            return ResponseEntity.badRequest().body("Wrong old password!");
        }

        traineeService.changePassword(changePasswordRequest.getUsername(), changePasswordRequest.getNewPassword());

        return ResponseEntity.ok("Password changed!");
    }

}
