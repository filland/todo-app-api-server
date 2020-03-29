package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.user.ChangeUserPasswordRQ;
import com.kurbatov.todoapp.dto.user.UpdateUserRQ;
import com.kurbatov.todoapp.dto.user.UserConverter;
import com.kurbatov.todoapp.dto.user.UserResource;
import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.dao.UserRepository;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User findEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
    }

    @Override
    public UserResource findById(Long userId) {
        User user = findEntityById(userId);
        return UserConverter.TO_RESOURCE.apply(user);
    }

    @Override
    public UserResource findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
        return UserConverter.TO_RESOURCE.apply(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
    }

    @Override
    public void existsByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new TodoAppException(ErrorType.USER_WITH_USERNAME_EXISTS, username);
        }
    }

    @Override
    public void existsByEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new TodoAppException(ErrorType.USER_WITH_EMAIL_EXISTS, email);
        }
    }

    @Override
    public UserResource updateCurrentUser(UpdateUserRQ updateUserRQ, UserDetails userDetails) {

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        User user = findEntityById(customUserDetails.getUserId());

        user.setFirstName(updateUserRQ.getFirstName());
        user.setLastName(updateUserRQ.getLastName());
        user.setUsername(updateUserRQ.getUsername());
        user.setEmail(updateUserRQ.getEmail());

        return UserConverter.TO_RESOURCE.apply(userRepository.save(user));
    }

    @Override
    public void updateCurrentUserPassword(ChangeUserPasswordRQ rq, UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        User user = findEntityById(customUserDetails.getUserId());

        // if provided password does not match actual user password
        if (!passwordEncoder.matches(rq.getOldPassword(), user.getPassword())) {
            throw new TodoAppException(ErrorType.BAD_CREDENTIALS);
        }

        String newEncodedPassword = passwordEncoder.encode(rq.getNewPassword());
        user.setPassword(newEncodedPassword);
        userRepository.save(user);
    }
}
