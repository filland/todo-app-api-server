package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.PageableRS;
import com.kurbatov.todoapp.dto.tag.CreateTagRQ;
import com.kurbatov.todoapp.dto.tag.TagConverter;
import com.kurbatov.todoapp.dto.todo.CreateTodoRQ;
import com.kurbatov.todoapp.dto.todo.TodoConverter;
import com.kurbatov.todoapp.dto.todo.TodoResource;
import com.kurbatov.todoapp.dto.todo.UpdateTodoRQ;
import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.dao.TagRepository;
import com.kurbatov.todoapp.persistence.dao.TodoRepository;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private TagRepository tagRepository;

    private Todo findEntityById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "Todo"));
    }

    @Override
    public TodoResource findById(long id) {
        Todo todo = findEntityById(id);
        return TodoConverter.TO_RESOURCE.apply(todo);
    }

    @Override
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public TodoResource createTodo(CreateTodoRQ createTodoRQ, UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Todo todo = new Todo();
        todo.setTitle(createTodoRQ.getTitle());
        todo.setDescription(createTodoRQ.getDescription());
        todo.setOwner(new User(customUserDetails.getUserId()));
        todo.setActive(true);
        todo.setDone(false);
        return TodoConverter.TO_RESOURCE.apply(todoRepository.save(todo));
    }

    @Override
    public TodoResource updateTodo(Long todoId, UpdateTodoRQ updateTodoRQ) {
        Todo dbTodo = findEntityById(todoId);
        dbTodo.setTitle(updateTodoRQ.getTitle());
        dbTodo.setDescription(updateTodoRQ.getDescription());
        dbTodo.setDone(updateTodoRQ.isDone());
        dbTodo.setActive(updateTodoRQ.isActive());
        return TodoConverter.TO_RESOURCE.apply(todoRepository.save(dbTodo));
    }

    @Override
    public void markTodoAsDone(Long todoId) {
        Todo todo = findEntityById(todoId);
        todo.setDone(true);
        todoRepository.save(todo);
    }

    @Override
    public void delete(Long todoId) {
        Todo todo = findEntityById(todoId);
        todo.setActive(false);
        todoRepository.save(todo);
    }

    @Override
    public List<Todo> findSeveral(Integer page, Integer limit, Long userId) {
        return todoRepository.findSeveral(page, limit, userId);
    }

    @Override
    public PageableRS<TodoResource> findSeveral(Long userId, Pageable pageable) {

        Page<Todo> todosPage = todoRepository
                .findAllByOwnerAndActive(new User(userId), true, pageable);

        List<TodoResource> todoResources = new ArrayList<>();
        for (Todo todo : todosPage.getContent()) {
            todoResources.add(TodoConverter.TO_RESOURCE.apply(todo));
        }

        PageableRS<TodoResource> todoPageableRS = new PageableRS<>();
        todoPageableRS.setList(todoResources);
        todoPageableRS.setTotalPages(todosPage.getTotalPages());
        todoPageableRS.setPage(pageable.getPageNumber());
        todoPageableRS.setSize(pageable.getPageSize());
        return todoPageableRS;
    }

    @Override
    public TodoResource addExistingTag(Long todoId, Long tagId) {
        Todo todo = findEntityById(todoId);

        for (Tag tag : todo.getTags()) {
            if (tag.getTagId().equals(tagId)) {
                throw new TodoAppException(ErrorType.INCORRECT_REQUEST, "The tag with id " + tagId + " already added");
            }
        }
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "Tag"));
        todo.getTags().add(tag);


        return TodoConverter.TO_RESOURCE.apply(createTodo(todo));
    }

    @Override
    @Transactional
    public TodoResource addNewTag(Long todoId, CreateTagRQ createTagRQ, CustomUserDetails userDetails) {
        Tag tag = new Tag();
        tag.setName(createTagRQ.getName());
        tag.setOwner(new User(userDetails.getUserId()));
        tag.setActive(true);
        Tag savedTag = tagRepository.save(tag);

        Todo todo = findEntityById(todoId);
        todo.getTags().add(savedTag);

        return TodoConverter.TO_RESOURCE.apply(todoRepository.save(todo));
    }

}
