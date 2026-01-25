package edu.mnim2377.gyak_spring.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingsService {

    private static final Logger logger = LoggerFactory.getLogger(GreetingsService.class);

    @Value("${app.greetings-message}")
    private String message;

    // Ez a metódus fut le "induláskor" (a Bean létrejötte után azonnal)
    @PostConstruct
    public void printGreeting() {
        logger.info("--- GREETING SERVICE INDUL ---");
        logger.info("Az uzenet: " + message);
        logger.info("------------------------------");
    }

    public void getMessage() {
        logger.info("message");
    }
}
