package com.kurbatov.todoapp.security.abac;

public final class AppPermission {

    public static final String TODO_OWNER_PERMISSION_NAME = "todoOwnerPermission";
    public static final String TODO_OWNER =
            "hasPermission(#todoId, '"+TODO_OWNER_PERMISSION_NAME+"')";

    public static final String USER_OWNER_PERMISSION_NAME = "userOwnerPermission";
    public static final String USER_OWNER =
            "hasPermission(#userId, '"+ USER_OWNER_PERMISSION_NAME +"')";

    public static final String TAG_OWNER_PERMISSION_NAME = "tagOwnerPermission";
    public static final String TAG_OWNER =
            "hasPermission(#tagId, '"+ TAG_OWNER_PERMISSION_NAME +"')";
}
