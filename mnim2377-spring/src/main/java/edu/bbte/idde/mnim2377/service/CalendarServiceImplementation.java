package edu.bbte.idde.mnim2377.service;


import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.repository.CalendarRepository;
import edu.bbte.idde.mnim2377.repository.exception.RepositoryException;
import edu.bbte.idde.mnim2377.service.exception.ServiceException;
import edu.bbte.idde.mnim2377.service.exception.ServiceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CalendarServiceImplementation implements CalendarService {
    private final CalendarRepository calendarRepository;

    public CalendarServiceImplementation(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    @Override
    public List<Calendar> getAllCalendars() {
        log.info("Retrieving all calendars");
        return calendarRepository.findAll();
    }

    @Override
    public void addCalendar(Calendar calendar) {
        log.info("Adding new calendar");
        calendarRepository.create(calendar);
    }

    @Override
    public void updateCalendar(Calendar calendar) throws ServiceException {
        try {
            log.info("Updating calendar with ID: {}", calendar.getId());
            calendarRepository.update(calendar);
        } catch (RepositoryException e) {
            log.warn("Update failed: {}", e.getMessage());
            throw new ServiceNotFoundException("Calendar not found for update", e);
        }
    }

    @Override
    public void deleteCalendar(UUID id) throws ServiceException {
        try {
            log.info("Deleting calendar with ID: {}", id);
            calendarRepository.deleteById(id);
        } catch (RepositoryException e) {
            log.warn("Delete failed: {}", e.getMessage());
            throw new ServiceNotFoundException("Calendar not found for deletion", e);
        }
    }

    @Override
    public Calendar getCalendarById(UUID id) throws ServiceException {
        log.info("Getting calendar by ID: {}", id);

        return calendarRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Calendar not found with ID: " + id));
    }
}
