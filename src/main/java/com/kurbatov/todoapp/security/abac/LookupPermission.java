package com.kurbatov.todoapp.security.abac;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specifies list of permission names to be assigned to some
 * {@link Permission} implementation<br>
 * <b>BE AWARE</b> that each permissions should be marked with
 * {@link LookupPermission} annotation
 * to be assigned to some permission name. Without this permission will be
 * ignored by {@link org.springframework.security.access.PermissionEvaluator}
 */
@Target(TYPE)
@Retention(RUNTIME)
@Documented
public @interface LookupPermission {
    String[] value();
}