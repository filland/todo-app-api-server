package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.service.TodoService;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TODO_OWNER)
    public Todo findTodo(@PathVariable Long todoId) {
        return todoService.find(todoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Todo> findSeveral(@RequestParam("page") Integer page,
                                  @RequestParam("limit") Integer limit,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.findSeveral(page, limit, userDetails.getUserId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Long saveTodo(@RequestBody Todo todo,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        return todoService.save(todo, userDetails);
    }

    @PutMapping(value = "/{todoId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TODO_OWNER)
    public Todo updateTodo(@RequestBody Todo todo, @PathVariable Long todoId,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.update(todo, userDetails);
    }

    @PutMapping(value = "/{todoId}/done")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize((TODO_OWNER))
    public void markTodoAsDone(@PathVariable Long todoId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        todoService.markTodoAsDone(todoId);
    }

    @DeleteMapping("/{todoId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TODO_OWNER)
    public void removeTodo(@PathVariable("todoId") Long todoId) {
        todoService.delete(todoId);
    }

}
