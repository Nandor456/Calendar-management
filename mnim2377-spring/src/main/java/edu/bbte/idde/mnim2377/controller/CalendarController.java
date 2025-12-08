package edu.bbte.idde.mnim2377.controller;


import edu.bbte.idde.mnim2377.dto.CalendarDto;
import edu.bbte.idde.mnim2377.dto.CalendarDtoExtended;
import edu.bbte.idde.mnim2377.mapper.CalendarMapper;
import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.service.CalendarService;
import edu.bbte.idde.mnim2377.service.exception.ServiceException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/calendars")
@Slf4j
public class CalendarController {
    private final CalendarService calendarService;
    private final CalendarMapper calendarMapper;

    public CalendarController(CalendarService calendarService, CalendarMapper calendarMapper) {
        this.calendarService = calendarService;
        this.calendarMapper = calendarMapper;
    }

    @GetMapping
    public List<CalendarDtoExtended> getAllCalendars() {
        log.info("REST request to get all calendars");
        return calendarMapper.toDtos(calendarService.getAllCalendars());
    }

    @GetMapping("/{id}")
    public CalendarDtoExtended getCalendarById(@PathVariable UUID id) throws ServiceException {
        log.info("REST request to get calendar by ID: {}", id);
        return calendarMapper.toDto(calendarService.getCalendarById(id));
    }

    @PostMapping
    public ResponseEntity<CalendarDtoExtended> createCalendar(@Valid @RequestBody CalendarDto calendarDto) {
        log.info("REST request to create calendar: {}", calendarDto);

        Calendar model = calendarMapper.toModel(calendarDto);

        calendarService.addCalendar(model);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarMapper.toDto(model));
    }

    @PutMapping("/{id}")
    public void updateCalendar(@PathVariable UUID id, @Valid @RequestBody CalendarDto calendarDto)
            throws ServiceException {
        log.info("REST request to update calendar ID: {}", id);

        Calendar model = calendarMapper.toModel(id, calendarDto);
        calendarService.updateCalendar(model);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCalendar(@PathVariable UUID id) throws ServiceException {
        log.info("REST request to delete calendar ID: {}", id);
        calendarService.deleteCalendar(id);
    }
}
