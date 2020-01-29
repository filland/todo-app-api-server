package com.kurbatov.todoapp.exception;

import java.util.HashMap;
import java.util.Map;

public class DefaultExceptionsResponses {

    public final static Map<Class<?>, RestException> defaultExceptionsMapping;

    static {
        defaultExceptionsMapping = new HashMap<>();
        // TODO populate the exceptionMap responses to default Spring's exceptions

//        exceptionMap.put(HttpMessageNotReadableException.class,
//                new RestErrorDefinition(400, ErrorType.INCORRECT_REQUEST));
//        exceptionMap.put(MissingServletRequestPartException.class, new RestErrorDefinition<>(400, ErrorType.INCORRECT_REQUEST, DEFAULT_MESSAGE_BUILDER));
//        exceptionMap.put(MissingServletRequestParameterException.class, new RestErrorDefinition<>(400, ErrorType.INCORRECT_REQUEST, DEFAULT_MESSAGE_BUILDER));
//        exceptionMap.put(AccessDeniedException.class, new RestErrorDefinition<>(403, ErrorType.ACCESS_DENIED, DEFAULT_MESSAGE_BUILDER));
//        exceptionMap.put(BadCredentialsException.class, new RestErrorDefinition<>(401, ErrorType.ACCESS_DENIED, DEFAULT_MESSAGE_BUILDER));
//        exceptionMap.put(LockedException.class, new RestErrorDefinition<>(403, ErrorType.ADDRESS_LOCKED, DEFAULT_MESSAGE_BUILDER));
//
//        exceptionMap.put(RestClientException.class, new RestErrorDefinition<>(400, ErrorType.UNABLE_INTERACT_WITH_INTEGRATION, DEFAULT_MESSAGE_BUILDER));
//
//        exceptionMap.put(Throwable.class, new RestErrorDefinition<>(500, ErrorType.UNCLASSIFIED_ERROR, DEFAULT_MESSAGE_BUILDER));
    }

}
