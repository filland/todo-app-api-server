package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.UpdateUserRQ;
import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.dao.UserRepository;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, UpdateUserRQ updateUserRQ, UserDetails userDetails) {

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        User user = this.findById(customUserDetails.getUserId());

        user.setUsername(updateUserRQ.getUsername());
        user.setFirstName(updateUserRQ.getFirstName());
        user.setLastName(updateUserRQ.getLastName());
        user.setEmail(updateUserRQ.getEmail());

        return this.saveUser(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
