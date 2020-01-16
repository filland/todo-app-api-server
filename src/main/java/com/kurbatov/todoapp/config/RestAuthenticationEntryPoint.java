package com.kurbatov.todoapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This bean is needed for responding with 401 to requests of unauthorized users
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        LOGGER.error("Unauthorized request.", e);
        response.getWriter().write(HttpServletResponse.SC_UNAUTHORIZED+" Unauthorized");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
