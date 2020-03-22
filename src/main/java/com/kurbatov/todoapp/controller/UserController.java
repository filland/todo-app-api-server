package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.dto.ChangeUserPasswordRQ;
import com.kurbatov.todoapp.dto.UpdateUserRQ;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public User getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = new User();
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getUserEmail());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        return user;
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public User updateCurrentUser(@Valid @RequestBody UpdateUserRQ user,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.updateCurrentUser(user, userDetails);
    }

    @PostMapping("/me/password")
    @ResponseStatus(HttpStatus.OK)
    public void changeCurrentUserPassword(@Valid @RequestBody ChangeUserPasswordRQ rq,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.updateCurrentUserPassword(rq, userDetails);
    }

    @GetMapping("/check-username")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity checkIfUsernameAlreadyUsed(@RequestParam("username") String username) {
        boolean usernameExists = userService.existsByUsername(username);
        if (usernameExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity checkIfEmailAlreadyUsed(@RequestParam("email") String email) {
        boolean emailExists = userService.existsByEmail(email);
        if (emailExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}
