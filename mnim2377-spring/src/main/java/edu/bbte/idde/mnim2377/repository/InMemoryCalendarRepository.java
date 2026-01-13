package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.repository.exception.RepositoryException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Repository
@Profile("in-memory")
public class InMemoryCalendarRepository implements CalendarRepository {

    private final Map<UUID, Calendar> calendarMap = new ConcurrentHashMap<>();

    @Override
    public List<Calendar> findAll() {
        return new ArrayList<>(calendarMap.values());
    }

    @Override
    public Calendar save(Calendar calendar) {
        calendarMap.put(calendar.getId(), calendar);
        return calendar;
    }

    @Override
    public void deleteById(UUID id) throws RepositoryException {
        if (!calendarMap.containsKey(id)) {
                throw new RepositoryException("Cant delete: ID:" + id + "cant be found");
        }
        calendarMap.remove(id);
    }

    @Override
    public Optional<Calendar> findById(UUID id) {
        if (!calendarMap.containsKey(id)) {
            throw new RepositoryException("Cant find: ID:" + id + "cant be found");
        }
        return Optional.of(calendarMap.get(id));
    }

    @Override
    public Optional<Calendar> findWithEventsById(UUID id) {
        return findById(id);
    }

    @Override
    public List<Calendar> findByDate(java.time.LocalDate date) {
        List<Calendar> result = new ArrayList<>();
        for (Calendar calendar : calendarMap.values()) {
            if (calendar.getDate().equals(date)) {
                result.add(calendar);
            }
        }
        return result;
    }
}
