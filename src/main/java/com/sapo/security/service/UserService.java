package com.sapo.security.service;

import com.sapo.security.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void save(User user);
    Iterable<User> findAll();
    User findByUsername(String username);
    boolean checkLogin(User user);
}
