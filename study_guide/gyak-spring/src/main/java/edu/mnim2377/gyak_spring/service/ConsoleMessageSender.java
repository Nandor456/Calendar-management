package edu.mnim2377.gyak_spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("console")
public class ConsoleMessageSender implements MessageSender {
    private final static Logger log = LoggerFactory.getLogger(ConsoleMessageSender.class);

    @Override
    public void sendMessage() {
        log.info("CONSOLE:");
    }
}
