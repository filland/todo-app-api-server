package com.kurbatov.todoapp;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(value = {"com.kurbatov.todoapp"})
public class TestConfig {

}
