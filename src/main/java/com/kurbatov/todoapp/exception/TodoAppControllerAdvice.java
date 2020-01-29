package com.kurbatov.todoapp.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class TodoAppControllerAdvice {

    @Autowired
    @Qualifier("todoAppExceptionResolver")
    private ExceptionResolver exceptionResolver;

    @Autowired
    @Qualifier("defaultExceptionResolver")
    private ExceptionResolver defaultResolver;

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleException(HttpServletRequest req, Exception ex) {

        RestException restException = exceptionResolver.resolveException(ex);

        if (restException != null) {
            return ResponseEntity
                    .status(restException.getHttpStatus())
                    .body(restException);
        } else {

            restException = defaultResolver.resolveException(ex);

            return ResponseEntity
                    .status(restException.getHttpStatus())
                    .body(restException.getErrorMessage());
        }
    }

}
