package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.repository.exception.RepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("jpa")
@RequiredArgsConstructor
@Slf4j
public class JpaCalendarRepository implements CalendarRepository {

    private final SpringDataCalendarDao dao;

    @Override
    public List<Calendar> findAll() {
        log.debug("JPA: Fetching all calendars");
        return dao.findAll();
    }

    @Override
    public Calendar create(Calendar calendar) {
        log.info("JPA: Saving new calendar");
        return dao.save(calendar);
    }

    @Override
    public void update(Calendar calendar) throws RepositoryException {
        if (!dao.existsById(calendar.getId())) {
            log.warn("JPA: Update failed. ID {} not found", calendar.getId());
            throw new RepositoryException("Cannot update: ID " + calendar.getId() + " not found");
        }
        dao.save(calendar);
        log.info("JPA: Updated calendar {}", calendar.getId());
    }

    @Override
    public void deleteById(UUID id) throws RepositoryException {
        if (!dao.existsById(id)) {
            log.warn("JPA: Delete failed. ID {} not found", id);
            throw new RepositoryException("Cannot delete: ID " + id + " not found");
        }
        dao.deleteById(id);
        log.info("JPA: Deleted calendar {}", id);
    }

    @Override
    public Optional<Calendar> findById(UUID id) {
        return dao.findById(id);
    }

    @Override
    public List<Calendar> findByDate(LocalDate date) {
        return dao.findByDate(date);
    }
}