package com.kurbatov.todoapp.security.abac.impl;

import com.kurbatov.todoapp.persistence.dao.UserService;
import com.kurbatov.todoapp.security.abac.LookupPermission;
import com.kurbatov.todoapp.security.abac.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

//@Component
@LookupPermission("userOwnerPermission")
public class UserOwnerPermission implements Permission {

    @Autowired
    private UserService userService;

    @Override
    public boolean isAllowed(Authentication authentication, Object targetDomainObject) {
        return false;
    }
}
