package com.kurbatov.todoapp.security.abac.impl;

import com.kurbatov.todoapp.security.CustomUserDetails;
import com.kurbatov.todoapp.security.abac.LookupPermission;
import com.kurbatov.todoapp.security.abac.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static com.kurbatov.todoapp.security.abac.AppPermission.USER_OWNER_PERMISSION_NAME;

/**
 * User can perform CRUD operations only on his/her own user info
 */
@Component
@LookupPermission(USER_OWNER_PERMISSION_NAME)
public class UserOwnerPermission implements Permission {

    @Override
    public boolean isAllowed(Authentication authentication, Object userId) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUserId() == userId;
    }
}
