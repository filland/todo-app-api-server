package com.kurbatov.todoapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// TODO consider removing commented configs
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Autowired
//    private ObjectMapper objectMapper;

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

//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
//        return new MappingJackson2HttpMessageConverter(objectMapper);
//    }
//
//    @Bean
//    public JsonMapper jsonMapper() {
//        return new JsonMapper();
//    }
//
//    @Bean
//    public RestExceptionBuilder restExceptionBuilder() {
//        return new RestExceptionBuilder();
//    }
}
