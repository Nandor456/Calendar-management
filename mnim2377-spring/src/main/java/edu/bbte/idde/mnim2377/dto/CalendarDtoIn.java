package edu.bbte.idde.mnim2377.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarDtoIn {

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    @NotNull(message = "Online status cannot be blank")
    private Boolean online;
}
