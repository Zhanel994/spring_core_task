package com.gym.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

//service to generate and parse JWT token
@Service
public class JwtService {

    @Value("${jwt.secret}") //to check token
    private String secret;

    public String generateToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes()); //turns String into a cryptographic key

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) //token lives 1 day
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parser()
                .verifyWith(key) //checks if token is real
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
