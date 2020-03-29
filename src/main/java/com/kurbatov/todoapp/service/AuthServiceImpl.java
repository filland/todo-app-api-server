package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.ConfirmEmailRQ;
import com.kurbatov.todoapp.dto.LoginRQ;
import com.kurbatov.todoapp.dto.RegisterUserRQ;
import com.kurbatov.todoapp.dto.RegisterUserRS;
import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.dao.UserRepository;
import com.kurbatov.todoapp.persistence.entity.ConfirmationToken;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.Role;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.JwtTokenProvider;
import com.kurbatov.todoapp.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${todoapp.email-confirmation-url}")
    private String emailConfirmationUrl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Transactional
    @Override
    public JwtAuthenticationResponse loginUser(LoginRQ loginRQ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRQ.getUsername(),
                        loginRQ.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        String tokenType = "Bearer";
        return new JwtAuthenticationResponse(jwt, tokenType);
    }

    @Transactional
    @Override
    public RegisterUserRS registerUser(RegisterUserRQ registerUserRQ) {
        // validate that user does not exist
        userService.existsByUsername(registerUserRQ.getUsername());
        userService.existsByEmail(registerUserRQ.getEmail());

        // Creating user's account
        User user = new User();
        user.setUsername(registerUserRQ.getUsername());
        user.setEmail(registerUserRQ.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserRQ.getPassword()));
        user.setRole(Role.USER.getAuthority());
        user.setActive(true);
        user.setEmailConfirmed(false);

        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            throw new TodoAppException(ErrorType.USER_ACCOUNT_WAS_NOT_CREATED);
        }

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(user);
        confirmationToken.setToken(token);
        confirmationToken.setActive(true);
        confirmationTokenService.save(confirmationToken);

        String emailConfirmationUrlWithToken = emailConfirmationUrl + "?token=" + token;
        emailService.sendRegistrationConfirmationEmail(
                "Confirm your email to complete registration", user.getEmail(), emailConfirmationUrlWithToken);

        RegisterUserRS registerUserRS = new RegisterUserRS();
        registerUserRS.setId(user.getId());
        return registerUserRS;
    }

    @Transactional
    @Override
    public void confirmEmail(ConfirmEmailRQ confirmEmailRQ) {
        ConfirmationToken token = confirmationTokenService.findByToken(confirmEmailRQ.getToken())
                .orElseThrow(() -> new TodoAppException(ErrorType.CONFIRMATION_TOKEN_NOT_FOUND));
        token.setActive(false);
        confirmationTokenService.save(token);

        User user = userRepository.findByEmail(token.getUser().getEmail())
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "User"));
        user.setEmailConfirmed(true);
        userRepository.save(user);
    }
}
