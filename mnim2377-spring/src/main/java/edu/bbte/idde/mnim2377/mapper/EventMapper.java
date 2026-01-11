package edu.bbte.idde.mnim2377.mapper;

import edu.bbte.idde.mnim2377.dto.EventDtoIn;
import edu.bbte.idde.mnim2377.dto.EventDtoOut;
import edu.bbte.idde.mnim2377.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    EventDtoOut toDto(Event event);

    List<EventDtoOut> toDtos(List<Event> events);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "calendar", ignore = true)
    Event toModel(EventDtoIn dto);
}

