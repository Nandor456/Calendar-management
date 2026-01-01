package edu.bbte.idde.mnim2377.repository.jpa;

import edu.bbte.idde.mnim2377.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataCalendarJpaRepository extends JpaRepository<Calendar, UUID> {
    List<Calendar> findByDate(LocalDate date);
}
