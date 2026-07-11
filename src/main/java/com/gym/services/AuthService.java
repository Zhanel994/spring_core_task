package com.gym.services;

import com.gym.dto.responses.JwtResponse;
import com.gym.exceptions.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//service for user authentication
@Service
public class AuthService {
    private static final int MAX_ATTEMPTS = 3; //maximum 3 wrong attempts

    private final AuthenticationManager authenticationManager; //username and password checker
    private final JwtService jwtService; //generates jwt token

    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>(); //the amount of wrong attempts
    private final Map<String, LocalDateTime> blockedUsers = new ConcurrentHashMap<>(); //the time when user gets unblocked
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet(); //JWT token which you cannot use (logout)

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public JwtResponse authenticate(String username, String password) {
        if (isBlocked(username)) {
            throw new AuthenticationException("User is blocked for 5 minutes"); //if user is blocked
        }

        //login attempt
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );

            //if login was successful, it resets attempts,
            failedAttempts.remove(username);
            blockedUsers.remove(username);

            //generates token
            String token = jwtService.generateToken(username);

            return new JwtResponse(token);
        } catch (org.springframework.security.core.AuthenticationException ex) {
            int attempts = failedAttempts.getOrDefault(username, 0) + 1; //attempt gets higher

            failedAttempts.put(username, attempts); //saves attempts

            if (attempts >= MAX_ATTEMPTS) {
                blockedUsers.put(
                        username,
                        LocalDateTime.now().plusMinutes(5) //blocks for 5 min
                );
            }
            throw new AuthenticationException("Invalid username or password");
        }
    }

    private boolean isBlocked(String username) {
        LocalDateTime blockedUntil = blockedUsers.get(username);

        if (blockedUntil == null) {
            return false;
        }

        //if blocked time is over, user gets unblocked
        if (blockedUntil.isBefore(LocalDateTime.now())) {
            blockedUsers.remove(username);
            failedAttempts.remove(username);
            return false;
        }
        return true;
    }

    public void logout(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
