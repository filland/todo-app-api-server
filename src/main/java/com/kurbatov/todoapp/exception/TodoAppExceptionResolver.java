package com.kurbatov.todoapp.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This resolver resolves exceptions which extends {@link TodoAppException}
 */
@Component("todoAppExceptionResolver")
public class TodoAppExceptionResolver implements ExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoAppExceptionResolver.class);

    private ExceptionResolver defaultExceptionResolver;

    public TodoAppExceptionResolver(ExceptionResolver defaultExceptionResolver) {
        this.defaultExceptionResolver = defaultExceptionResolver;
    }

    @Override
    public RestException resolveException(Exception ex) {

        LOGGER.error("TodoAppExceptionResolver > " + ex.getMessage(), ex);

        if (ex instanceof TodoAppException) {

            TodoAppException todoAppException = (TodoAppException) ex;

            return new RestExceptionBuilder()
                    .setHttpStatus(todoAppException.getErrorType().getHttpStatus())
                    .setErrorMessage(todoAppException.getMessage())
                    .setErrorType(todoAppException.getErrorType())
                    .build();
        } else {
            return defaultExceptionResolver.resolveException(ex);
        }
    }
}
