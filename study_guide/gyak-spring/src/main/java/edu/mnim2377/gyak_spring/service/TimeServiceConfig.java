package edu.mnim2377.gyak_spring.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeServiceConfig {

    @Bean
    public TimeService timeService() {
        return new TimeService();
    }
}
