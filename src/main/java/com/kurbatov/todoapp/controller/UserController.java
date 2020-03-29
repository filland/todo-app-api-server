package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.dto.user.ChangeUserPasswordRQ;
import com.kurbatov.todoapp.dto.user.UpdateUserRQ;
import com.kurbatov.todoapp.dto.user.UserResource;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public UserResource getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // do not use services since userDetails already has all info
        UserResource user = new UserResource();
        user.setId(userDetails.getUserId());
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getUserEmail());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setActive(userDetails.isActive());
        return user;
    }

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResource updateCurrentUser(@Valid @RequestBody UpdateUserRQ user,
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
    public void checkIfUsernameAlreadyUsed(@RequestParam("username") String username) {
        userService.existsByUsername(username);
    }

    @GetMapping("/check-email")
    @ResponseStatus(HttpStatus.OK)
    public void checkIfEmailAlreadyUsed(@RequestParam("email") String email) {
        userService.existsByEmail(email);
    }
}
