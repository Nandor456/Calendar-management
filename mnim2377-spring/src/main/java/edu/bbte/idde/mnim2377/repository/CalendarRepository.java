package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CalendarRepository {
    // create + update helyett:
    Calendar save(Calendar calendar);

    // Ezt már ismeri a Spring Data:
    List<Calendar> findAll();

    // Ezt is ismeri:
    Optional<Calendar> findById(UUID id);

    // Ezt is ismeri:
    void deleteById(UUID id);

    // Ez egyedi metódus, de a JpaRepository-ban definiálhatjuk
    Optional<Calendar> findWithEventsById(UUID id);

    // Query method, a Spring Data ebből tud SQL-t generálni a neve alapján
    List<Calendar> findByDate(LocalDate date);
}