package com.gym.config;

import com.gym.services.JwtService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//aAutomatically adds a JWT token to all Feign requests
@Configuration
public class FeignJwtConfig {
    @Bean
    public RequestInterceptor requestInterceptor(JwtService jwtService) {

        return template -> {

            String token = jwtService.generateToken("gym-service");

            template.header(
                    "Authorization",
                    "Bearer " + token
            );

        };
    }
}
