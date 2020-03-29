package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.dto.PageableRS;
import com.kurbatov.todoapp.dto.tag.CreateTagRQ;
import com.kurbatov.todoapp.dto.todo.CreateTodoRQ;
import com.kurbatov.todoapp.dto.todo.TodoResource;
import com.kurbatov.todoapp.dto.todo.UpdateTodoRQ;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.kurbatov.todoapp.security.abac.AppPermission.TAG_OWNER;
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
    public TodoResource findTodo(@PathVariable Long todoId) {
        return todoService.findById(todoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageableRS<TodoResource> findSeveral(@PageableDefault(sort = {"updated"}) Pageable pageable,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.findSeveral(userDetails.getUserId(), pageable);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResource createTodo(@Valid @RequestBody CreateTodoRQ rq,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.createTodo(rq, userDetails);
    }

    @PutMapping(value = "/{todoId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TODO_OWNER)
    public TodoResource updateTodo(@Valid @RequestBody UpdateTodoRQ updateTodoRQ,
                                   @PathVariable Long todoId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.updateTodo(todoId, updateTodoRQ);
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
    public void deleteTodo(@PathVariable("todoId") Long todoId) {
        todoService.delete(todoId);
    }

    @PostMapping(value = "/{todoId}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(TODO_OWNER)
    public TodoResource addNewTag(@Valid @RequestBody CreateTagRQ tag,
                                  @PathVariable Long todoId,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.addNewTag(todoId, tag, userDetails);
    }

    @PostMapping(value = "/{todoId}/tags/{tagId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(TODO_OWNER + "&&" + TAG_OWNER)
    public TodoResource addExistingTag(@PathVariable Long todoId, @PathVariable Long tagId) {
        return todoService.addExistingTag(todoId, tagId);
    }
}
