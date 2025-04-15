package com.spring.security.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@SpringBootApplication
public class MsSpringSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsSpringSecurityJwtApplication.class, args);
    }
    @Bean
    public HandlerMappingIntrospector handlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
}
