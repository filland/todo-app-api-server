package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.dto.ConfirmEmailRQ;
import com.kurbatov.todoapp.dto.LoginRQ;
import com.kurbatov.todoapp.dto.RegisterRQ;
import com.kurbatov.todoapp.dto.RegisterRS;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    public JwtAuthenticationResponse login(@Valid @RequestBody LoginRQ loginRQ) {
        return authService.loginUser(loginRQ);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterRS register(@Valid @RequestBody RegisterRQ registerRQ) {
        return authService.registerUser(registerRQ);
    }

    @PostMapping("/confirm-email")
    @ResponseStatus(HttpStatus.OK)
    public void confirmEmailToCompleteRegistration(@RequestBody ConfirmEmailRQ confirmEmailRQ) {
        authService.confirmEmail(confirmEmailRQ);
    }

}
