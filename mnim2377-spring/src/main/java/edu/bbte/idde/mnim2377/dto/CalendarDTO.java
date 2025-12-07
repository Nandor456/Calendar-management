package edu.bbte.idde.mnim2377.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CalendarDTO {
    private UUID id;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotBlank(message = "Date cannot be blank")
    private LocalDate date;

    @NotBlank(message = "Online status cannot be blank")
    private Boolean online;
}
