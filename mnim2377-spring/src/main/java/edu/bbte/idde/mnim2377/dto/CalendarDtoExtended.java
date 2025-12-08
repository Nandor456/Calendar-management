package edu.bbte.idde.mnim2377.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CalendarDtoExtended extends CalendarDto {

    @NotNull(message = "ID cannot be null")
    private UUID id;
}
