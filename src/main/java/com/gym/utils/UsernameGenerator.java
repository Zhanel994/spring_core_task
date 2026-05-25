package com.gym.utils;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//utility class for generating unique usernames
@Component
public class UsernameGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    public String generate(String firstName, String lastName) {
        String base = firstName + "." + lastName;
        String username = base;

        int counter = 1;

        while (exists(username)) {
            username = base + counter;
            counter++;
        }

        return username;
    }

    private boolean exists(String username) {
        Long count = entityManager.createQuery(
                                "SELECT COUNT(u) FROM User u " +
                                        "WHERE u.username=:u",
                                Long.class)
                        .setParameter("u", username)
                        .getSingleResult();
        return count > 0;
    }
}
