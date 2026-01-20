package edu.bbte.idde.mnim2377.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class EventDtoIn {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private LocalTime startTime;

    private LocalTime endTime;
}
