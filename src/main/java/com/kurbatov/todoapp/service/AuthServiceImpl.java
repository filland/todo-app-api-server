package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.Role;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.JwtTokenProvider;
import com.kurbatov.todoapp.security.jwt.SignInRQ;
import com.kurbatov.todoapp.security.jwt.SignUpRQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationResponse loginUser(SignInRQ signInRQ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRQ.getUsernameOrEmail(),
                        signInRQ.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public void registerUser(SignUpRQ signUpRQ) {
        if (userService.existsByUsername(signUpRQ.getUsername())) {
            throw new TodoAppException(ErrorType.USER_WITH_USERNAME_EXISTS, signUpRQ.getUsername());
        }

        if (userService.existsByEmail(signUpRQ.getEmail())) {
            throw new TodoAppException(ErrorType.USER_WITH_EMAIL_EXISTS, signUpRQ.getEmail());
        }

        // Creating user's account
        User user = new User();
        user.setUsername(signUpRQ.getUsername());
        user.setEmail(signUpRQ.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRQ.getPassword()));
        user.setRole(Role.USER.getAuthority());
        user.setActive(true);

        User savedUser = userService.saveUser(user);

        if (savedUser == null) {
            throw new TodoAppException(ErrorType.USER_ACCOUNT_WAS_NOT_CREATED);
        }
    }
}
