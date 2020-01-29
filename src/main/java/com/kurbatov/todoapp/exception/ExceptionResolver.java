package com.kurbatov.todoapp.exception;

/**
 * Custom exception resolver
 */
public interface ExceptionResolver {

    /**
     * Create RestException from provided exception
     *
     * @param ex Exception to be resolved
     * @return Resolved {@link RestException}
     */
    RestException resolveException(Exception ex);

}
