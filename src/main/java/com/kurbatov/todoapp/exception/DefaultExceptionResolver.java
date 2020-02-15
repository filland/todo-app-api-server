package com.kurbatov.todoapp.exception;

import org.springframework.stereotype.Component;

import static com.kurbatov.todoapp.exception.DefaultExceptionsResponses.defaultExceptionsMapping;


/**
 * This exception resolver resolves all exceptions that
 * do not extends {@link TodoAppException}
 */
@Component("defaultExceptionResolver")
public class DefaultExceptionResolver implements ExceptionResolver {

    @Override
    public RestException resolveException(Exception ex) {

        RestException restException = defaultExceptionsMapping.get(ex.getClass());

        if (restException != null) {

            return restException;
        } else {
            return new RestExceptionBuilder()
//                    .setErrorMessage(ex.getMessage())
                    .setErrorType(ErrorType.INCORRECT_REQUEST)
                    .build();
        }

    }
}
