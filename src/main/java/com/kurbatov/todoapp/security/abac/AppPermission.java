package com.kurbatov.todoapp.security.abac;

public final class AppPermission {

    public static final String TODO_OWNER_PERMISSION_NAME = "todoOwnerPermission";
    public static final String TODO_OWNER =
            "hasPermission(#todoId, '"+TODO_OWNER_PERMISSION_NAME+"')";

    public static final String USER_OWNER_PERMISSION_NAME = "userOwnerPermission";
    public static final String USER_OWNER =
            "hasPermission(#userId, '"+ USER_OWNER_PERMISSION_NAME +"')";


}
