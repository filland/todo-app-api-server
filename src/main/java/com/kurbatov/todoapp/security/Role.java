package com.kurbatov.todoapp.security;

/**
 * FYI
 * RP has the following levels:
 * Project Role: OPERATOR(0), CUSTOMER(1), MEMBER(2), PROJECT_MANAGER(3);
 * User role: USER, ADMINISTRATOR;
 * Set of permissions: com.epam.ta.reportportal.auth.permissions.Permissions
 */
public enum Role {
    USER(1),
    ADMIN(2);

    public static final String ROLE_PREFIX = "ROLE_";

    private int roleLevel;

    Role(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public String getAuthority() {
        return ROLE_PREFIX + this.name();
    }
}
