package com.kurbatov.todoapp.security.permissions;

import com.kurbatov.todoapp.util.ApplicationContextAwareFactoryBean;
import org.springframework.security.access.PermissionEvaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Factory bean for providing permissions marked with {@link LookupPermission}
 * annotation
 */
public class PermissionEvaluatorFactoryBean extends ApplicationContextAwareFactoryBean<PermissionEvaluator> {

    @Override
    public Class<?> getObjectType() {
        return PermissionEvaluator.class;
    }

    @Override
    protected PermissionEvaluator createInstance() {

        /*
         * Find all beans in context marked with LookupPermission annotation
         */
        Map<String, Object> permissionBeans = getApplicationContext().getBeansWithAnnotation(LookupPermission.class);
        Map<String, Permission> permissionsMap = new HashMap<>();
        for (Entry<String, Object> permission : permissionBeans.entrySet()) {
            /*
             * There will be no NPE since we asked bean factory to get beans
             * with this annotation
             */
            for (String permissionName : permission.getValue().getClass().getAnnotation(LookupPermission.class).value()) {
                if (Permission.class.isAssignableFrom(permission.getValue().getClass())) {
                    /*
                     * Assign permission name from LookupPermission annotation
                     * to it's value
                     */
                    permissionsMap.put(permissionName, (Permission) permission.getValue());
                }
            }
        }

        return new TodoAppPermissionEvaluator(permissionsMap);
    }
}