package edu.bbte.idde.mnim2377.backend.service;

import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.exception.DataException;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceException;

import java.util.List;

public class CalendarServiceImplementation implements CalendarService {
    private final CalendarDao calendarDao;

    public CalendarServiceImplementation(CalendarDao calendarDao) {
        this.calendarDao = calendarDao;
    }

    @Override
    public List<Calendar> getAllCalendars() {
        return calendarDao.findAll();
    }

    @Override
    public void addCalendar(Calendar calendar) {
        calendarDao.create(calendar);
    }

    @Override
    public void updateCalendar(Calendar calendar) throws ServiceException {
        try {
            calendarDao.update(calendar);
        } catch (DataException e) {
            throw new ServiceException("Service exception during update", e);
        }
    }

    @Override
    public void deleteCalendar(String id) throws ServiceException {
        try {
            calendarDao.delete(id);
        } catch (DataException e) {
            throw new ServiceException("Service exception during delete", e);
        }
    }

    @Override
    public Calendar getCalendarById(String id) throws ServiceException {
        try {
            return calendarDao.getCalendarById(id);
        } catch (DataException e) {
            throw new ServiceException("Service exception during getting calendar", e);
        }
    }
}
