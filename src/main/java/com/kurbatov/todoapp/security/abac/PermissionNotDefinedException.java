package com.kurbatov.todoapp.security.abac;

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
