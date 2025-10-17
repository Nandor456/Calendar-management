package edu.bbte.idde.mnim2377.service;

import edu.bbte.idde.mnim2377.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.data.model.Calendar;
import edu.bbte.idde.mnim2377.service.exception.CalendarNotFoundException;

import java.util.List;

public class CalendarService {
    private final CalendarDao calendarDao;
    public CalendarService(CalendarDao calendarDao) {
        this.calendarDao = calendarDao;
    }
    
    public List<Calendar> getAllCalendars() {
        return calendarDao.findAll();
    }

    public void addCalendar(Calendar calendar) {
        calendarDao.create(calendar);
    }

    public void updateCalendar(Calendar calendar) throws CalendarNotFoundException {
        calendarDao.update(calendar);
    }

    public void deleteCalendar(String id) throws CalendarNotFoundException {
        calendarDao.delete(id);
    }

    public Calendar getCalendarById(String id) throws CalendarNotFoundException {
        return calendarDao.getCalendarById(id);
    }
}
