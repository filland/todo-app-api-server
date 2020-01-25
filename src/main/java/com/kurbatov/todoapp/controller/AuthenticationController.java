package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.persistence.dao.UserService;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.Role;
import com.kurbatov.todoapp.security.jwt.JwtAuthenticationResponse;
import com.kurbatov.todoapp.security.jwt.JwtTokenProvider;
import com.kurbatov.todoapp.security.jwt.SignInRQ;
import com.kurbatov.todoapp.security.jwt.SignUpRQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles authentication using username/password
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SignInRQ signInRQ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRQ.getUsernameOrEmail(),
                        signInRQ.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity register(@RequestBody SignUpRQ signUpRQ) {
        if (userService.existsByUsername(signUpRQ.getUsername())) {
            return new ResponseEntity("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userService.existsByEmail(signUpRQ.getEmail())) {
            return new ResponseEntity("Email Address already in use!", HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User();
        user.setUsername(signUpRQ.getUsername());
        user.setEmail(signUpRQ.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRQ.getPassword()));
        user.setRole(Role.USER.getAuthority());
        user.setActive(true);

        User savedUser = userService.saveUser(user);

        return savedUser == null ?
                new ResponseEntity("User account was not created.", HttpStatus.BAD_REQUEST) :
                new ResponseEntity(HttpStatus.CREATED);
    }


}
