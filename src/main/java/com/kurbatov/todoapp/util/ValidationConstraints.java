package com.kurbatov.todoapp.util;

/**
 * Contains constants for defining validation constraints.
 */
public class ValidationConstraints {

    // tag
    public static final int MIN_TAG_NAME_LENGTH = 1;
    public static final int MAX_TAG_NAME_LENGTH = 20;

    // todo_
    public static final int MIN_TODO_TITLE_LENGTH = 0;
    public static final int MAX_TODO_TITLE_LENGTH = 300;
    public static final int MIN_TODO_DESC_LENGTH = 0;
    public static final int MAX_TODO_DESC_LENGTH = 4_000;

    // user
    public static final int MAX_USER_FIRST_NAME_LENGTH = 50;
    public static final int MAX_USER_LAST_NAME_LENGTH = 50;
    public static final int MIN_USERNAME_LENGTH = 5;
    public static final int MAX_USERNAME_LENGTH = 30;
    public static final int MIN_USER_PASSWORD_LENGTH = 6;
    public static final int MAX_USER_PASSWORD_LENGTH = 30;

}
