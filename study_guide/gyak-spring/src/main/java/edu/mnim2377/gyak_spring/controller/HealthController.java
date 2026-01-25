package edu.mnim2377.gyak_spring.controller;

import edu.mnim2377.gyak_spring.dto.TimeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<TimeDto> getDate() {
        TimeDto timeDto = new TimeDto(LocalDate.now(), Status.UP);
        return ResponseEntity.ok(timeDto);
    }
}
