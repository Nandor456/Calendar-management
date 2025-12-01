package edu.bbte.idde.mnim2377.backend.service;

import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.exception.DataException;
import edu.bbte.idde.mnim2377.backend.data.exception.NotFoundException;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceException;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceNotFoundException;

import java.util.List;
import java.util.UUID;

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
        } catch (NotFoundException e) { // translate DAO layer not-found into service-layer not-found
            throw new ServiceNotFoundException(e.getMessage());
        } catch (DataException e) {
            throw new ServiceException("Service exception during update", e);
        }
    }

    @Override
    public void deleteCalendar(UUID id) throws ServiceException {
        try {
            calendarDao.delete(id);
        } catch (NotFoundException e) {
            throw new ServiceNotFoundException(e.getMessage());
        } catch (DataException e) {
            throw new ServiceException("Service exception during delete", e);
        }
    }

    @Override
    public Calendar getCalendarById(UUID id) throws ServiceException {
        try {
            return calendarDao.getCalendarById(id);
        } catch (NotFoundException e) {
            throw new ServiceNotFoundException(e.getMessage());
        } catch (DataException e) {
            throw new ServiceException("Service exception during getting calendar", e);
        }
    }
}
