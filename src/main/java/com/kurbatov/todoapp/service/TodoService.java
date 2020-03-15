package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.common.PageableRS;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TodoService {

    Todo find(long id);

    Todo save(Todo todo);

    Todo save(Todo todo, UserDetails userDetails);

    Todo update(Long todoId, Todo todo);

    void markTodoAsDone(Long todoId);

    void delete(Long todoId);

    List findSeveral(Integer page, Integer limit, Long userId);

    PageableRS<Todo> findSeveral(Long userId, Pageable pageable);

    /**
     * Adds the existing tag to the todo_
     * @param todoId the id of todo_ to add the existing tag to
     * @param tagId the id of existing tag
     */
    Todo addExistingTag(Long todoId, Long tagId);

    /**
     * Creates a new tag and adds it to the todo_
     * @param todoId the id of todo_ to add a new tag to
     * @param tag a tag to be created
     * @param userDetails
     * @return
     */
    Todo addNewTag(Long todoId, Tag tag, CustomUserDetails userDetails);
}
