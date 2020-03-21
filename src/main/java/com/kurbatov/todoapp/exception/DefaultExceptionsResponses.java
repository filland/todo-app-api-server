package com.kurbatov.todoapp.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

import static com.kurbatov.todoapp.util.StringUtils.formatMessage;

public class DefaultExceptionsResponses {

    public final static Map<Class<?>, RestException> defaultExceptionsMapping;

    static {
        defaultExceptionsMapping = new HashMap<>();

        defaultExceptionsMapping.put(
                HttpMessageNotReadableException.class,
                buildRestException(ErrorType.INCORRECT_REQUEST, "Http message is not readable"));

        defaultExceptionsMapping.put(
                MissingServletRequestPartException.class,
                buildRestException(ErrorType.INCORRECT_REQUEST, "A request part is missing"));

        defaultExceptionsMapping.put(
                MissingServletRequestParameterException.class,
                buildRestException(ErrorType.INCORRECT_REQUEST, "A request param is missing"));

        defaultExceptionsMapping.put(
                AccessDeniedException.class,
                buildRestException(ErrorType.ACCESS_DENIED, null));

        defaultExceptionsMapping.put(
                BadCredentialsException.class,
                buildRestException(ErrorType.BAD_CREDENTIALS, null));

        defaultExceptionsMapping.put(
                LockedException.class,
                buildRestException(ErrorType.ADDRESS_LOCKED, null));

        defaultExceptionsMapping.put(
                Throwable.class,
                buildRestException(ErrorType.UNCLASSIFIED_ERROR, null));


    }

    private static RestException buildRestException(ErrorType errorType, String message) {
        return new RestExceptionBuilder()
                .setErrorMessage(message == null ?
                        errorType.getDescription() :
                        formatMessage(errorType.getDescription(), message))
                .setErrorType(errorType)
                .build();
    }

}
