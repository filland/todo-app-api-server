package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.PageableRS;
import com.kurbatov.todoapp.dto.tag.CreateTagRQ;
import com.kurbatov.todoapp.dto.todo.CreateTodoRQ;
import com.kurbatov.todoapp.dto.todo.TodoResource;
import com.kurbatov.todoapp.dto.todo.UpdateTodoRQ;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TodoService {

    TodoResource findById(long id);

    Todo createTodo(Todo todo);

    TodoResource createTodo(CreateTodoRQ createTodoRQ, UserDetails userDetails);

    TodoResource updateTodo(Long todoId, UpdateTodoRQ updateTodoRQ);

    void markTodoAsDone(Long todoId);

    void delete(Long todoId);

    List findSeveral(Integer page, Integer limit, Long userId);

    PageableRS<TodoResource> findSeveral(Long userId, Pageable pageable);

    /**
     * Adds the existing tag to the todo_
     *
     * @param todoId the id of todo_ to add the existing tag to
     * @param tagId  the id of existing tag
     * @return
     */
    TodoResource addExistingTag(Long todoId, Long tagId);

    /**
     * Creates a new tag and adds it to the todo_
     *
     * @param todoId      the id of todo_ to add a new tag to
     * @param tag         a tag to be created
     * @param userDetails
     * @return
     */
    TodoResource addNewTag(Long todoId, CreateTagRQ tag, CustomUserDetails userDetails);
}
