package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.SignInRQ;
import com.kurbatov.todoapp.security.jwt.SignUpRQ;
import com.kurbatov.todoapp.service.AuthService;
import com.kurbatov.todoapp.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles authentication using username/password
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationResponse login(@RequestBody SignInRQ signInRQ) {
        return authService.loginUser(signInRQ);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody SignUpRQ signUpRQ) {
        authService.registerUser(signUpRQ);
    }

    @GetMapping("/confirm-email")
    @ResponseStatus(HttpStatus.OK)
    public void confirmEmail(@RequestParam("token") String confirmationToken) {
        emailService.confirmEmail(confirmationToken);
    }

}
