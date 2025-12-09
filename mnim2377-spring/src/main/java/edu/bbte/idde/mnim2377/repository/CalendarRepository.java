package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.repository.exception.RepositoryException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalendarRepository {
    List<Calendar> findAll();

    Calendar create(Calendar calendar);

    void update(Calendar calendar) throws RepositoryException;

    void deleteById(UUID id) throws RepositoryException;

    Optional<Calendar> findById(UUID id);

    List<Calendar> findByDate(LocalDate date);
}
