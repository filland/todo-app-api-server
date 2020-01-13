package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.persistence.dao.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity userPasswordRegistration() {

        

        return new ResponseEntity(HttpStatus.CREATED);
    }
}