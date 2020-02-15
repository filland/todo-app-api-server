package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.persistence.entity.User;

import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
