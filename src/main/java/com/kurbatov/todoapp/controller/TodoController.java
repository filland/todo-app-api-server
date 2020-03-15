package com.kurbatov.todoapp.controller;

import com.kurbatov.todoapp.dto.common.PageableRS;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Todo findTodo(@PathVariable Long todoId) {
        return todoService.find(todoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageableRS<Todo> findSeveral(@PageableDefault(sort = {"todoId"}, direction = Sort.Direction.DESC) Pageable pageable,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.findSeveral(userDetails.getUserId(), pageable);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Todo saveTodo(@RequestBody Todo todo,
                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.save(todo, userDetails);
    }

    @PutMapping(value = "/{todoId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(TODO_OWNER)
    public Todo updateTodo(@RequestBody Todo todo, @PathVariable Long todoId,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.update(todoId, todo);
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

    @PostMapping(value = "/{todoId}/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(TODO_OWNER)
    public Todo addNewTag(@RequestBody Tag tag, @PathVariable long todoId,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        return todoService.addNewTag(todoId, tag, userDetails);
    }

    @PostMapping(value = "/{todoId}/tags/{tagId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(TODO_OWNER + "&&" + TAG_OWNER)
    public Todo addExistingTag(@PathVariable Long todoId, @PathVariable Long tagId) {
        return todoService.addExistingTag(todoId, tagId);
    }
}
