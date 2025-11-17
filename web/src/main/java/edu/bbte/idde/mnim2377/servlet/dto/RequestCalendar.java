package edu.bbte.idde.mnim2377.servlet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RequestCalendar {
    @NotBlank(message = "Address cannot be blank")
    public String address;

    @NotBlank(message = "Location cannot be blank")
    public String location;

    @NotNull(message = "Date is required")
    public LocalDate date;

    @NotNull(message = "Online status is required")
    public Boolean online;
}