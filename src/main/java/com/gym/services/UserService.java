package com.gym.services;

import com.gym.dao.UserDao;
import com.gym.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//service to connect the user db with the Spring auth system
@Service
public class UserService implements UserDetailsService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //to find the user
        User user = userDao.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));

        return org.springframework.security.core.userdetails.User
                                                                .withUsername(user.getUsername())
                                                                .password(user.getPassword())
                                                                .authorities("USER") //user role
                                                                .disabled(!user.isActive())
                                                                .build();
    }
}
