package edu.bbte.idde.mnim2377.data.dao;

import edu.bbte.idde.mnim2377.data.model.Calendar;
import edu.bbte.idde.mnim2377.service.exception.CalendarNotFoundException;

import java.util.*;

public class InMemoryCalendarDao implements CalendarDao {
    private final Map<String, Calendar> calendarMap = new HashMap<>();

    public List<Calendar> findAll() {
        return new ArrayList<>(calendarMap.values());
    }

    @Override
    public void create(Calendar calendar) {
        calendarMap.put(calendar.getId(), calendar);
    }

    @Override
    public void update(Calendar calendar) throws CalendarNotFoundException {
        if (!calendarMap.containsKey(calendar.getId())) {
            throw new CalendarNotFoundException("Cant update: ID:" + calendar.getId() + "cant be found");
        }
        calendarMap.put(calendar.getId(), calendar);
    }

    @Override
    public void delete(String id) throws CalendarNotFoundException {
        if (!calendarMap.containsKey(id)) {
            throw new CalendarNotFoundException("Cant delete: ID:" + id + "cant be found");
        }
        calendarMap.remove(id);
    }

    public Calendar getCalendarById(String id) throws CalendarNotFoundException{
        if (!calendarMap.containsKey(id)) {
            throw new CalendarNotFoundException("Cant find: ID:" + id + "cant be found");
        }
        return calendarMap.get(id);
    }
}
