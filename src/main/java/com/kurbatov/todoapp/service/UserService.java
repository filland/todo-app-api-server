package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.ChangeUserPasswordRQ;
import com.kurbatov.todoapp.dto.UpdateUserRQ;
import com.kurbatov.todoapp.persistence.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {

    User findByUsername(String username);

    User findById(Long userId);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User saveUser(User user);

    /**
     * Updates user info of the authenticated user
     */
    User updateCurrentUser(UpdateUserRQ updateUserRQ, UserDetails userDetails);

    void updateCurrentUserPassword(ChangeUserPasswordRQ rq, UserDetails userDetails);
}
