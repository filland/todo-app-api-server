package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.CompleteRegistrationRQ;
import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.entity.ConfirmationToken;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.Role;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.JwtTokenProvider;
import com.kurbatov.todoapp.security.jwt.SignInRQ;
import com.kurbatov.todoapp.security.jwt.SignUpRQ;
import com.kurbatov.todoapp.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

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

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

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
    @Transactional
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
        user.setEmailConfirmed(false);

        User savedUser = userService.saveUser(user);

        if (savedUser == null) {
            throw new TodoAppException(ErrorType.USER_ACCOUNT_WAS_NOT_CREATED);
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationToken.setToken(token);
        confirmationToken.setActive(true);
        confirmationTokenService.save(confirmationToken);

        String confirmationUrl = signUpRQ.getEmailConfirmationBrowserUrl() + "?token=" + token;
        emailService.sendRegistrationConfirmationEmail(
                "User registration confirmation", user.getEmail(), confirmationUrl);
    }

    @Override
    @Transactional
    public void completeRegistration(CompleteRegistrationRQ completeRegistrationRQ) {
        ConfirmationToken token = confirmationTokenService.findByToken(completeRegistrationRQ.getToken())
                .orElseThrow(() -> new TodoAppException(ErrorType.CONFIRMATION_TOKEN_NOT_FOUND));
        token.setActive(false);
        confirmationTokenService.save(token);

        User user = userService.findByEmail(token.getUser().getEmail())
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
        user.setEmailConfirmed(true);
        userService.saveUser(user);
    }
}
