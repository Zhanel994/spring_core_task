package com.gym.controllers;

import com.gym.dto.requests.ChangePasswordRequest;
import com.gym.services.AuthService;
import com.gym.services.TraineeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//REST Controller for auth
@RestController
@RequestMapping("api/v1/auth")
@Api(tags = "Authentication API")
public class AuthController {
    private final AuthService authService;
    private final TraineeService traineeService;

    public AuthController(AuthService authService, TraineeService traineeService) {
        this.authService = authService;
        this.traineeService = traineeService;
    }

    @GetMapping("/login")
    @ApiOperation("Login user")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        boolean result = authService.authenticate(username, password);
        if(!result) {
            return ResponseEntity.badRequest().body("Invalid credentials!");
        }

        return ResponseEntity.ok("Login successful!");
    }

    @PutMapping("/change-password")
    @ApiOperation("Change Password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        boolean authenticated = authService.authenticate(changePasswordRequest.getUsername(), changePasswordRequest.getOldPassword());

        if(!authenticated) {
            return ResponseEntity.badRequest().body("Wrong old password!");
        }

        traineeService.changePassword(changePasswordRequest.getUsername(), changePasswordRequest.getNewPassword());

        return ResponseEntity.ok("Password changed!");
    }

}
