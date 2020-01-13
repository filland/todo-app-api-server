package com.kurbatov.todoapp.security.permissions;

public class PermissionNotDefinedException extends RuntimeException {

    public PermissionNotDefinedException() {
    }

    public PermissionNotDefinedException(String message) {
        super(message);
    }

    public PermissionNotDefinedException(String message, Throwable cause) {
        super(message, cause);
    }
}
