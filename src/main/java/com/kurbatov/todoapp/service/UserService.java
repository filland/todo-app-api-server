package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.user.ChangeUserPasswordRQ;
import com.kurbatov.todoapp.dto.user.UpdateUserRQ;
import com.kurbatov.todoapp.dto.user.UserResource;
import com.kurbatov.todoapp.persistence.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserResource findByUsername(String username);

    UserResource findById(Long userId);

    User findByEmail(String email);

    /**
     * Does nothing if user does not exist then .
     * Throw an exception if a user with the username exists
     */
    void existsByUsername(String username);

    /**
     * Does nothing if user does not exist
     * Throw an exception if user exists
     */
    void existsByEmail(String email);

    /**
     * Updates user info of the authenticated user
     */
    UserResource updateCurrentUser(UpdateUserRQ updateUserRQ, UserDetails userDetails);

    void updateCurrentUserPassword(ChangeUserPasswordRQ rq, UserDetails userDetails);
}
