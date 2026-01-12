package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.repository.exception.RepositoryException;
import edu.bbte.idde.mnim2377.repository.jpa.SpringDataCalendarJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("jpa")
@Slf4j
public class JpaCalendarRepository implements CalendarRepository {

    private final SpringDataCalendarJpaRepository springDataRepo;

    public JpaCalendarRepository(SpringDataCalendarJpaRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public List<Calendar> findAll() {
        log.info("Fetching all calendars via JPA");
        return springDataRepo.findAll();
    }

    @Override
    public Calendar create(Calendar calendar) {
        log.info("Creating calendar via JPA (id={})", calendar.getId());
        return springDataRepo.save(calendar);
    }

    @Override
    public void update(Calendar calendar) throws RepositoryException {
        UUID id = calendar.getId();
        if (id == null) {
            throw new RepositoryException("Cannot update entity without id");
        }
        if (!springDataRepo.existsById(id)) {
            throw new RepositoryException("No calendar found with ID: " + id);
        }
        springDataRepo.save(calendar);
    }

    @Override
    public void deleteById(UUID id) throws RepositoryException {
        if (!springDataRepo.existsById(id)) {
            throw new RepositoryException("No calendar found with ID: " + id);
        }
        springDataRepo.deleteById(id);
    }

    @Override
    public Optional<Calendar> findById(UUID id) {
        return springDataRepo.findById(id);
    }

    @Override
    public List<Calendar> findByDate(LocalDate date) {
        return springDataRepo.findByDate(date);
    }

    @Override
    public Optional<Calendar> findWithEventsById(UUID id) {
        return springDataRepo.findWithEventsById(id);
    }
}
