package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.UpdateUserRQ;
import com.kurbatov.todoapp.persistence.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    User updateUser(Long userId, UpdateUserRQ updateUserRQ, UserDetails userDetails);

    User findByUsername(String username);

    User findById(Long userId);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
