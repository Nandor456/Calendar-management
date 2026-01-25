package edu.mnim2377.gyak_spring.dto;

import edu.mnim2377.gyak_spring.controller.Status;


import java.time.LocalDate;

public record TimeDto(LocalDate timestamp, Status status) {}