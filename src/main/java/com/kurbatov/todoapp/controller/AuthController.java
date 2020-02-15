package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.dto.CompleteRegistrationRQ;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.LoginRQ;
import com.kurbatov.todoapp.security.jwt.RegisterRQ;
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
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationResponse login(@RequestBody LoginRQ loginRQ) {
        return authService.loginUser(loginRQ);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegisterRQ registerRQ) {
        authService.registerUser(registerRQ);
    }

    @PostMapping("/complete-registration")
    @ResponseStatus(HttpStatus.OK)
    public void completeRegistration(@RequestBody CompleteRegistrationRQ completeRegistrationRQ) {
        authService.completeRegistration(completeRegistrationRQ);
    }

}
