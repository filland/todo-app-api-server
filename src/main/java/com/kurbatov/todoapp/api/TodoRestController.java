package com.kurbatov.todoapp.api;

import com.kurbatov.todoapp.persistence.Todo;
import com.kurbatov.todoapp.persistence.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/v1/todos")
public class TodoRestController {

    @Autowired
    private TodoService todoService;

    @GetMapping("{todoID}")
    @ResponseStatus(HttpStatus.OK)
    public Todo findTodo(@PathVariable int todoID) {

        return todoService.find(todoID);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SuppressWarnings(value = "unchecked")
    public List<Todo> findSeveral(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {

        return todoService.findSeveral(page, limit);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> saveTodo(@RequestBody Todo todo) {

        long todoID = todoService.save(todo);

        return new ResponseEntity<>(todoID, HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Todo updateTodo(@RequestBody Todo todo) {

        return todoService.update(todo);
    }

    @DeleteMapping("{todoID}")
    @ResponseStatus(HttpStatus.OK)
    public void removeTodo(@PathVariable("todoID") long todoID) {

        todoService.delete(todoID);
    }

}
