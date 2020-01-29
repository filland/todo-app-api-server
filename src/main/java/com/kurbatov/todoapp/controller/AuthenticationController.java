package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.SignInRQ;
import com.kurbatov.todoapp.security.jwt.SignUpRQ;
import com.kurbatov.todoapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles authentication using username/password
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

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

}
