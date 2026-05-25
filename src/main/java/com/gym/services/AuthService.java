package com.gym.services;

import com.gym.exceptions.AuthenticationException;
import com.gym.models.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//service for user authentication
@Service
public class AuthService {

    @PersistenceContext
    private EntityManager entityManager;

    //authenticates user by username and password
    public boolean authenticate(String username, String password) {
        try {
            User user = entityManager.createQuery(
                                    "SELECT u FROM User u " +
                                            "WHERE u.username=:u", User.class)
                            .setParameter("u", username)
                            .getSingleResult();
            return user.getPassword().equals(password); //returns true if credentials are valid
        } catch (Exception exception) {
            throw new AuthenticationException("Invalid username or password!"); //returns exception if authentication fails
        }
    }
}
