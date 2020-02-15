package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.kurbatov.todoapp.security.abac.AppPermission.USER_OWNER;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(USER_OWNER)
    public User getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getUserEmail());
        return user;
    }

    // TODO does not work
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(USER_OWNER)
    public User updateUser(@PathVariable Long userId, @RequestBody User user,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.saveUser(user);
    }

    @GetMapping("/check-username")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity checkIfUsernameAlreadyUsed(@RequestParam("username") String username) {
        boolean usernameExists = userService.existsByUsername(username);

        // the username already used
        if (usernameExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // the provided username not used
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity checkIfEmailAlreadyUsed(@RequestParam("email") String email) {
        boolean emailExists = userService.existsByEmail(email);

        // the email already used
        if (emailExists) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // the provided email not used
        return ResponseEntity.ok().build();
    }
}
