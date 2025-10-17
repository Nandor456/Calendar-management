package edu.bbte.idde.mnim2377.data.dao;

import edu.bbte.idde.mnim2377.data.model.Calendar;
import edu.bbte.idde.mnim2377.service.exception.CalendarNotFoundException;

import java.util.List;

public interface CalendarDao {
    List<Calendar> findAll();
    Calendar getCalendarById(String id) throws CalendarNotFoundException;
    void create(Calendar calendar);
    void update(Calendar calendar) throws CalendarNotFoundException;
    void delete(String id) throws CalendarNotFoundException;
}
