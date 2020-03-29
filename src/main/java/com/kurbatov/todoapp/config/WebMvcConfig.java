package com.kurbatov.todoapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * // TODO What the heck ? com.kurbatov.todoapp.config.WebSecurityConfig#configure(HttpSecurity h). Try here
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

}
