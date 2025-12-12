package edu.bbte.idde.mnim2377.mapper;

import edu.bbte.idde.mnim2377.dto.CalendarDto;
import edu.bbte.idde.mnim2377.dto.CalendarDtoExtended;
import edu.bbte.idde.mnim2377.model.Calendar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CalendarMapper {
    CalendarDtoExtended toDto(Calendar calendar);

    // Used for Lists (GET /calendars)
    List<CalendarDtoExtended> toDtos(List<Calendar> calendars);

    // Used for CREATE (POST) - No ID in input
    @Mapping(target = "id", ignore = true)
    Calendar toModel(CalendarDto calendarDto);

    //Must preserve ID from path
    default Calendar toModel(UUID id, CalendarDto calendarDto) {
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
