package com.kurbatov.todoapp.exception;

import com.kurbatov.todoapp.dto.ValidationRS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TodoAppControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoAppControllerAdvice.class);

    @Autowired
    @Qualifier("todoAppExceptionResolver")
    private ExceptionResolver exceptionResolver;

    @Autowired
    @Qualifier("defaultExceptionResolver")
    private ExceptionResolver defaultResolver;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleNotValid(HttpServletRequest req, Exception ex) {

        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;

        Map<String, String> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage));

        ValidationRS validationRS = new ValidationRS();
        validationRS.setErrors(errors);
        validationRS.setHttpStatus(ErrorType.INVALID_INFORMATION.getHttpStatus());
        validationRS.setErrorCode(ErrorType.INVALID_INFORMATION.getCode());

        return new ResponseEntity(validationRS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleException(HttpServletRequest req, Exception ex) {

        LOGGER.error(ex.getMessage(), ex);

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
