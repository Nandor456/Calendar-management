package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface SpringDataCalendarDao extends JpaRepository<Calendar, UUID> {
    List<Calendar> findByDate(LocalDate date);
}
