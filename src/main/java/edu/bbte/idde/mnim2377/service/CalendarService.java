package edu.bbte.idde.mnim2377.service;

import edu.bbte.idde.mnim2377.data.model.Calendar;
import edu.bbte.idde.mnim2377.service.exception.ServiceException;

import java.util.List;

public interface CalendarService {
    List<Calendar> getAllCalendars();
    void addCalendar(Calendar calendar);
    void updateCalendar(Calendar calendar) throws ServiceException;
    void deleteCalendar(String id) throws ServiceException;
    Calendar getCalendarById(String id) throws ServiceException;
}
