package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.persistence.dao.TodoService;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kurbatov.todoapp.security.abac.AppPermission.TODO_OWNER;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    private final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    @Autowired
    private TodoService todoService;

    @GetMapping("/{todoID}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TODO_OWNER)
    public Todo findTodo(@PathVariable Long todoID) {
        return todoService.find(todoID);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Todo> findSeveral(@RequestParam("page") Integer page,
                                  @RequestParam("limit") Integer limit,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        return todoService.findSeveral(page, limit, userDetails.getUserID());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> saveTodo(@RequestBody Todo todo,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        todo.setOwner(new User(userDetails.getUserID()));
        todo.setActive(true);
        long todoID = todoService.save(todo);

        return new ResponseEntity<>(todoID, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{todoID}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Todo updateTodo(@RequestBody Todo todo, @PathVariable Long todoID,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        todo.setOwner(new User(userDetails.getUserID()));
        return todoService.update(todo);
    }

    @DeleteMapping("/{todoID}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TODO_OWNER)
    public void removeTodo(@PathVariable("todoID") Long todoID) {

        todoService.delete(todoID);
    }

}
