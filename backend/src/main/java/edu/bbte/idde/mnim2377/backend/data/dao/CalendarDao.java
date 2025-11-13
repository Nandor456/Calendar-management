package edu.bbte.idde.mnim2377.backend.data.dao;

import edu.bbte.idde.mnim2377.backend.data.exception.DataException;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;

import java.util.List;

public interface CalendarDao {
    List<Calendar> findAll();

    Calendar getCalendarById(String id) throws DataException;

    void create(Calendar calendar);

    void update(Calendar calendar) throws DataException;

    void delete(String id) throws DataException;
}
