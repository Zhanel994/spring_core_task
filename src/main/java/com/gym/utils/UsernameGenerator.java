package com.gym.utils;

import com.gym.storage.Storage;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerator {
    private final Storage storage;

    public UsernameGenerator(Storage storage) {
        this.storage = storage;
    }

    public String generate(String firstName, String lastName) {
        String base = firstName + "." + lastName;
        String username = base;

        int counter = 1;

        while (storage.usernameExists(username)) {
            username = base + counter;
            counter++;
        }

        return username;
    }
}
