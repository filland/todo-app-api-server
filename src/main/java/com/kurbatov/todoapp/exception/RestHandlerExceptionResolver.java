package com.kurbatov.todoapp.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RestHandlerExceptionResolver extends DefaultHandlerExceptionResolver {

    private ExceptionResolver resolver;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Set of converters to be able to render response
     */
    private List<HttpMessageConverter<?>> messageConverters;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        ModelAndView defaultError = super.doResolveException(request, response, handler, ex);
        if (null != defaultError) {
            return defaultError;
        }

        return handleCustomException(request, response, ex);
    }

    protected ModelAndView handleCustomException(HttpServletRequest request, HttpServletResponse response, Exception ex) {

        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        RestException restException = resolver.resolveException(ex);

        if (restException == null) {
            return null;
        }
        applyStatusIfPossible(webRequest, restException.getHttpStatus());

        try {

            return handleResponseBody(restException, webRequest);

        } catch (Throwable e) {
            if (logger.isWarnEnabled()) {
                logger.warn("Unable to write error message", e);
            }
            return null;
        }

    }

    private void applyStatusIfPossible(ServletWebRequest webRequest, HttpStatus status) {
        if (!WebUtils.isIncludeRequest(webRequest.getRequest())) {
            webRequest.getResponse().setStatus(status.value());
        }
    }


    private ModelAndView handleResponseBody(Object body, ServletWebRequest webRequest) throws IOException {
        HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());

        List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
        if (acceptedMediaTypes.isEmpty()) {
            acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
        }

        MediaType.sortByQualityValue(acceptedMediaTypes);

        HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());

        Class<?> bodyType = body.getClass();

        List<HttpMessageConverter<?>> converters = this.messageConverters;

        if (converters != null) {
            for (MediaType acceptedMediaType : acceptedMediaTypes) {
                for (HttpMessageConverter messageConverter : converters) {
                    if (messageConverter.canWrite(bodyType, acceptedMediaType)) {
                        messageConverter.write(body, acceptedMediaType, outputMessage);
                        // return empty model and view to short circuit the
                        // iteration and to let
                        // Spring know that we've rendered the view ourselves:
                        return new ModelAndView();
                    }
                }
            }
        }

        if (logger.isWarnEnabled()) {
            logger.warn("Could not find HttpMessageConverter that supports return type [" + bodyType + "] and " + acceptedMediaTypes);
        }
        return null;

    }

    /**
     * Override default behavior and handle bind exception as custom exception
     */
    @Override
    protected ModelAndView handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request,
                                                                 HttpServletResponse response, Object handler) throws IOException {
        return handleCustomException(request, response, ex);
    }

    @Override
    protected ModelAndView handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request,
                                                        HttpServletResponse response, Object handler) throws IOException {
        return handleCustomException(request, response, ex);
    }

    @Override
    protected ModelAndView handleMissingServletRequestPartException(MissingServletRequestPartException ex, HttpServletRequest request,
                                                                    HttpServletResponse response, Object handler) throws IOException {
        return handleCustomException(request, response, ex);
    }

    @Override
    protected ModelAndView handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request,
                                                                HttpServletResponse response, Object handler) throws IOException {
        return handleCustomException(request, response, ex);
    }

    public void setResolver(ExceptionResolver resolver) {
        this.resolver = resolver;
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        this.messageConverters = messageConverters;
    }
}
