package edu.bbte.idde.mnim2377.data.dao;

import edu.bbte.idde.mnim2377.data.exception.DataException;
import edu.bbte.idde.mnim2377.data.model.Calendar;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCalendarDao implements CalendarDao {
    private final Map<String, Calendar> calendarMap = new ConcurrentHashMap<>();

    @Override
    public List<Calendar> findAll() {
        return new ArrayList<>(calendarMap.values());
    }

    @Override
    public void create(Calendar calendar) {
        calendarMap.put(calendar.getId(), calendar);
    }

    @Override
    public void update(Calendar calendar) throws DataException {
        if (!calendarMap.containsKey(calendar.getId())) {
            throw new DataException("Cant update: ID:" + calendar.getId() + "cant be found");
        }
        calendarMap.put(calendar.getId(), calendar);
    }

    @Override
    public void delete(String id) throws DataException {
        if (!calendarMap.containsKey(id)) {
            throw new DataException("Cant delete: ID:" + id + "cant be found");
        }
        calendarMap.remove(id);
    }

    @Override
    public Calendar getCalendarById(String id) throws DataException {
        if (!calendarMap.containsKey(id)) {
            throw new DataException("Cant find: ID:" + id + "cant be found");
        }
        return calendarMap.get(id);
    }
}
