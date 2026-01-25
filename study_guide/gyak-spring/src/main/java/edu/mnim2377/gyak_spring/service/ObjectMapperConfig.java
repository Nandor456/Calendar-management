package edu.mnim2377.gyak_spring.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Configuration
public class ObjectMapperConfig {

    @Bean
    @Lazy
    public ObjectMapper getObjectMapper() {
        System.out.println("\n>>> 1. AZ OBJECTMAPPER BEAN LÉTREJÖTT! <<<\n");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }
}
