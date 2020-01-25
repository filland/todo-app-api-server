package com.kurbatov.todoapp.security.abac;

import org.springframework.security.core.Authentication;

/**
 * Permission representation<br>
 * <b>BE AWARE</b> that each permissions should be marked with
 * {@link LookupPermission} annotation
 * to be assigned to some permission name. Without this permission will be
 * ignored by {@link org.springframework.security.access.PermissionEvaluator}
 */
public interface Permission<T> {


    /**
     * Is action allowed for user with {@link Authentication} for target object
     *
     * @param authentication
     * @param targetDomainObject
     * @return
     */
    boolean isAllowed(Authentication authentication, Object targetDomainObject);
}
