package com.kurbatov.todoapp.exception;

import static com.kurbatov.todoapp.util.StringUtils.formatMessage;

/**
 * Base TodoApp exception
 */
public class TodoAppException extends RuntimeException {

    private ErrorType errorType;
    private Object[] params;

    public TodoAppException(String message) {
        super(message);
    }

    public TodoAppException(ErrorType errorType, String... params) {
        super(formatMessage(errorType.getDescription(), params));
        this.errorType = errorType;
        this.params = params;
    }

    public TodoAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public Object[] getParams() {
        return params;
    }
}
