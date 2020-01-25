package com.kurbatov.todoapp.security.abac;

public final class AppPermission {

    public static final String TODO_OWNER_PERMISSION_NAME = "todoOwnerPermission";
    public static final String TODO_OWNER =
            "hasPermission(#todoID, '"+TODO_OWNER_PERMISSION_NAME+"')";


}
