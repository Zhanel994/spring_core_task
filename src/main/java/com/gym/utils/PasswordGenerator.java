package com.gym.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

//utility class for generating random password
@Component
public class PasswordGenerator {

    public String generate() {
        return UUID.randomUUID()
                .toString()
                .substring(0, 10);
    }
}