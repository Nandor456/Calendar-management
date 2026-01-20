package edu.bbte.idde.mnim2377.controller;


import edu.bbte.idde.mnim2377.dto.CalendarDtoIn;
import edu.bbte.idde.mnim2377.dto.CalendarDtoOut;
import edu.bbte.idde.mnim2377.dto.ErrorDto;
import edu.bbte.idde.mnim2377.dto.EventDtoIn;
import edu.bbte.idde.mnim2377.dto.EventDtoOut;
import edu.bbte.idde.mnim2377.mapper.CalendarMapper;
import edu.bbte.idde.mnim2377.mapper.EventMapper;
import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.model.Event;
import edu.bbte.idde.mnim2377.service.CalendarService;
import edu.bbte.idde.mnim2377.service.exception.ServiceException;
import edu.bbte.idde.mnim2377.service.exception.ServiceNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/calendars")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class CalendarController {
    private final CalendarService calendarService;
    private final CalendarMapper calendarMapper;
    private final EventMapper eventMapper;

    public CalendarController(CalendarService calendarService, CalendarMapper calendarMapper, EventMapper eventMapper) {
        this.calendarService = calendarService;
        this.calendarMapper = calendarMapper;
        this.eventMapper = eventMapper;
    }

    @GetMapping
    public List<CalendarDtoOut> getCalendars(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws ServiceException {
        if (date != null) {
            log.info("REST request to get calendars with date filter: {}", date);
            return calendarMapper.toDtos(calendarService.getCalendarsByDate(date));
        } else {
            log.info("REST request to get all calendars");
            return calendarMapper.toDtos(calendarService.getAllCalendars());
        }
    }

    @GetMapping("/{id}")
    public CalendarDtoOut getCalendarById(@PathVariable UUID id) throws ServiceException {
        log.info("REST request to get calendar by ID: {}", id);
        return calendarMapper.toDto(calendarService.getCalendarById(id));
    }

    @PostMapping
    public ResponseEntity<CalendarDtoOut> createCalendar(@Valid @RequestBody CalendarDtoIn calendarDto) {
        log.info("REST request to create calendar: {}", calendarDto);

        Calendar model = calendarMapper.toModel(calendarDto);

        calendarService.addCalendar(model);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarMapper.toDto(model));
    }

    @PutMapping("/{id}")
    public void updateCalendar(@PathVariable UUID id, @Valid @RequestBody CalendarDtoIn calendarDto)
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

    @GetMapping("/{calendarId}/events")
    public List<EventDtoOut> getEventsForCalendar(@PathVariable UUID calendarId) throws ServiceException {
        log.info("REST request to get events for calendar ID: {}", calendarId);
        return eventMapper.toDtos(calendarService.getEventsForCalendar(calendarId));
    }

    @PostMapping("/{calendarId}/events")
    public ResponseEntity<EventDtoOut> addEventToCalendar(
            @PathVariable UUID calendarId,
            @Valid @RequestBody EventDtoIn eventDto
    ) throws ServiceException {
        log.info("REST request to add event to calendar ID: {}", calendarId);

        Event event = eventMapper.toModel(eventDto);
        Event created = calendarService.addEventToCalendar(calendarId, event);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventMapper.toDto(created));
    }

    @DeleteMapping("/{calendarId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventFromCalendar(@PathVariable UUID calendarId, @PathVariable UUID eventId)
            throws ServiceException {
        log.info("REST request to delete event ID: {} from calendar ID: {}", eventId, calendarId);
        calendarService.deleteEventFromCalendar(calendarId, eventId);
    }

    @ExceptionHandler(ServiceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleServiceNotFoundException(ServiceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        ErrorDto body = new ErrorDto(
                ex.getMessage(),
                "NOT_FOUND",
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorDto> handleServiceException(ServiceException ex) {
        log.error("Internal server error: {}", ex.getMessage());
        ErrorDto body = new ErrorDto(
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
