package com.kurbatov.todoapp.service;

import com.kurbatov.todoapp.dto.common.PageableRS;
import com.kurbatov.todoapp.exception.ErrorType;
import com.kurbatov.todoapp.exception.TodoAppException;
import com.kurbatov.todoapp.persistence.dao.TodoRepository;
import com.kurbatov.todoapp.persistence.entity.Tag;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.persistence.entity.User;
import com.kurbatov.todoapp.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;

@Component
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TagService tagService;

    @Override
    public Todo find(long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoAppException(ErrorType.RESOURCE_NOT_FOUND, "Todo"));
    }

    @Override
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo save(Todo todo, UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        todo.setOwner(new User(customUserDetails.getUserId()));
        todo.setActive(true);
        return todoRepository.save(todo);
    }

    @Override
    public Todo update(Long todoId, Todo todo) {
        Todo dbTodo = this.find(todoId);
        dbTodo.setTitle(todo.getTitle());
        dbTodo.setDescription(todo.getDescription());
        dbTodo.setDone(todo.isDone());
        dbTodo.setActive(todo.isActive());
        return todoRepository.save(dbTodo);
    }

    @Override
    public void markTodoAsDone(Long todoId) {
        Todo todo = find(todoId);
        todo.setDone(true);
        todoRepository.save(todo);
    }

    @Override
    public void delete(Long todoId) {
        Todo todo = this.find(todoId);
        todo.setActive(false);
        todoRepository.save(todo);
    }

    @Override
    public List<Todo> findSeveral(Integer page, Integer limit, Long userId) {
        return todoRepository.findSeveral(page, limit, userId);
    }

    @Override
    public PageableRS<Todo> findSeveral(Long userId, Pageable pageable) {

        Page<Todo> todosPage = todoRepository.findAllByOwnerAndActive(new User(userId), true, pageable);

        PageableRS<Todo> todoPageableRS = new PageableRS<>();
        todoPageableRS.setList(todosPage.getContent());
        todoPageableRS.setTotalPages(todosPage.getTotalPages());
        todoPageableRS.setPage(pageable.getPageNumber());
        todoPageableRS.setSize(pageable.getPageSize());

        return todoPageableRS;
    }

    @Override
    public Todo addExistingTag(Long todoId, Long tagId) {
        Todo todo = this.find(todoId);

        for (Tag tag : todo.getTags()) {
            if (tag.getTagId().equals(tagId)) {
                throw new TodoAppException(ErrorType.INCORRECT_REQUEST, "The tag with id " + tagId + " already added");
            }
        }

        Tag tag = tagService.findById(tagId);
        todo.getTags().add(tag);
        return this.save(todo);
    }

    @Override
    @Transactional
    public Todo addNewTag(Long todoId, Tag tag, CustomUserDetails userDetails) {
        Todo todo = this.find(todoId);
        tag.setOwner(new User(userDetails.getUserId()));
        Tag savedTag = tagService.save(tag);
        todo.getTags().add(savedTag);
        return this.save(todo);
    }

}
