package com.kurbatov.todoapp.security.abac.impl;

import com.kurbatov.todoapp.service.TodoService;
import com.kurbatov.todoapp.persistence.entity.Todo;
import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.security.abac.LookupPermission;
import com.kurbatov.todoapp.security.abac.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.kurbatov.todoapp.security.abac.AppPermission.TODO_OWNER_PERMISSION_NAME;

@Component
@LookupPermission(TODO_OWNER_PERMISSION_NAME)
public class TodoOwnerPermission implements Permission {

    @Autowired
    private TodoService todoService;

    @Override
    public boolean isAllowed(Authentication authentication, Object todoID) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Todo dbTodo = todoService.find((Long) todoID);

        if (dbTodo == null) {
            return false;
        }

        return userDetails.getUserID().equals(dbTodo.getOwner().getUserID());
    }
}
