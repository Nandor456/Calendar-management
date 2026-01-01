package edu.bbte.idde.mnim2377.mapper;

import edu.bbte.idde.mnim2377.dto.CalendarDtoIn;
import edu.bbte.idde.mnim2377.dto.CalendarDtoOut;
import edu.bbte.idde.mnim2377.model.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CalendarMapper {
    CalendarDtoOut toDto(Calendar calendar);

    // Used for Lists (GET /calendars)
    List<CalendarDtoOut> toDtos(List<Calendar> calendars);

    // Used for CREATE (POST) - No ID in input
    Calendar toModel(CalendarDtoIn calendarDto);

    //Must preserve ID from path
    default Calendar toModel(UUID id, CalendarDtoIn calendarDto) {
        if (calendarDto == null) {
            return null;
        }
        return new Calendar(
                id,
                calendarDto.getAddress(),
                calendarDto.getLocation(),
                calendarDto.getDate(),
                calendarDto.getOnline()
        );
    }
}
