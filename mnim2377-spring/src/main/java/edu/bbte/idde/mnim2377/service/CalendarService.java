package edu.bbte.idde.mnim2377.service;


import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;

public interface CalendarService {
    List<Calendar> getAllCalendars();

    void addCalendar(Calendar calendar);

    void updateCalendar(Calendar calendar) throws ServiceException;

    void deleteCalendar(UUID id) throws ServiceException;

    Calendar getCalendarById(UUID id) throws ServiceException;
}
