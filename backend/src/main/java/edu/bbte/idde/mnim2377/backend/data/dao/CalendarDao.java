package edu.bbte.idde.mnim2377.backend.data.dao;

import edu.bbte.idde.mnim2377.backend.data.exception.DataException;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;

import java.util.List;
import java.util.UUID;

public interface CalendarDao {
    List<Calendar> findAll();

    Calendar getCalendarById(UUID id) throws DataException;

    void create(Calendar calendar);

    void update(Calendar calendar) throws DataException;

    void delete(UUID id) throws DataException;
}
