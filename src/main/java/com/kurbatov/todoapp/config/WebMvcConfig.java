package com.kurbatov.todoapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.kurbatov.todoapp.exception.DefaultExceptionResolver;
import com.kurbatov.todoapp.exception.RestExceptionBuilder;
import com.kurbatov.todoapp.exception.RestHandlerExceptionResolver;
import com.kurbatov.todoapp.exception.TodoAppExceptionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter converter;

    /**
     * CORS cannot be configured using CorsConfigurationSource
     * in the {@link com.kurbatov.todoapp.config.WebSecurityConfig}
     * in the configure(AuthenticationManagerBuilder auth) because
     * the project uses Spring MVC
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTION");
    }

//    @Override
//    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
//
//        RestHandlerExceptionResolver handler = new RestHandlerExceptionResolver();
//        handler.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
//
//        DefaultExceptionResolver defaultExceptionResolver = new DefaultExceptionResolver();
//
//        handler.setResolver(new TodoAppExceptionResolver(defaultExceptionResolver));
//        handler.setMessageConverters(Collections.singletonList(converter));
//
//        resolvers.add(handler);
//    }
//
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public JsonMapper jsonMapper() {
        return new JsonMapper();
    }


    @Bean
    public RestExceptionBuilder restExceptionBuilder() {
        return new RestExceptionBuilder();
    }
}
