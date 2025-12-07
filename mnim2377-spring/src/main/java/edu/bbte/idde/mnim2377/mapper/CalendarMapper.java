package edu.bbte.idde.mnim2377.mapper;

import edu.bbte.idde.mnim2377.dto.CalendarDTO;
import edu.bbte.idde.mnim2377.model.Calendar;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CalendarMapper {
    CalendarDTO toDto(Calendar calendar);

    Calendar toModel(CalendarDTO calendarDto);

    List<CalendarDTO> toDtos(List<Calendar> calendars);
}
