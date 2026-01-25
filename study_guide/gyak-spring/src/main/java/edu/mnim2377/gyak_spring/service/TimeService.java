package edu.mnim2377.gyak_spring.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeService {
    public String getFormattedTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
