package com.kurbatov.todoapp.security.permissions;

import com.kurbatov.todoapp.security.Role;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Map;


public class TodoAppPermissionEvaluator implements PermissionEvaluator {

    private static final GrantedAuthority ADMIN_AUTHORITY = new SimpleGrantedAuthority(Role.ADMIN.getAuthority());

    /**
     * Mapping between permission names and permissions
     */
    private Map<String, Permission> permissionNameToPermissionMap;

    private boolean allowAllToAdmin;

    public TodoAppPermissionEvaluator(Map<String, Permission> permissionNameToPermissionMap) {
        this.permissionNameToPermissionMap = permissionNameToPermissionMap;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;

        if (canHandle(authentication, targetDomainObject, permission)) {
            hasPermission = checkPermission(authentication, targetDomainObject, (String) permission);
        }
        return hasPermission;
    }

    private boolean canHandle(Authentication authentication, Object targetDomainObject, Object permission) {
        return targetDomainObject != null && authentication != null && String.class.equals(permission.getClass());
    }

    private boolean checkPermission(Authentication authentication, Object targetDomainObject, String permissionKey) {
        verifyPermissionIsDefined(permissionKey);
        if (allowAllToAdmin && authentication.isAuthenticated() &&
                authentication.getAuthorities().contains(ADMIN_AUTHORITY)) {
            return true;
        }
        Permission permission = permissionNameToPermissionMap.get(permissionKey);
        return permission.isAllowed(authentication, targetDomainObject);
    }

    private void verifyPermissionIsDefined(String permissionKey) {
        if (!permissionNameToPermissionMap.containsKey(permissionKey)) {
            throw new PermissionNotDefinedException(
                    "No permission with key " + permissionKey + " is defined in " + this.getClass().toString());
        }
    }


    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
