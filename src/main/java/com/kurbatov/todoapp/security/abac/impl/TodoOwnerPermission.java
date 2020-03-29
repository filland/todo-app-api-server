package com.kurbatov.todoapp.security.abac.impl;

import com.kurbatov.todoapp.dto.todo.TodoResource;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.security.abac.LookupPermission;
import com.kurbatov.todoapp.security.abac.Permission;
import com.kurbatov.todoapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.kurbatov.todoapp.security.abac.AppPermission.TODO_OWNER_PERMISSION_NAME;

/**
 * User can perform CRUD operations only on his/her own todos
 */
@Component
@LookupPermission(TODO_OWNER_PERMISSION_NAME)
public class TodoOwnerPermission implements Permission {

    @Autowired
    private TodoService todoService;

    @Override
    public boolean isAllowed(Authentication authentication, Object todoId) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        TodoResource todoResource = todoService.findById((Long) todoId);

        if (todoResource == null) {
            return false;
        }

        return userDetails.getUserId().equals(todoResource.getOwnerId());
    }
}
