package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.repository.exception.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("jdbc")
@Slf4j
public class JdbcCalendarRepository implements CalendarRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Calendar> calendarRowMapper = (rs, rowNum) -> {
        return new Calendar(
                UUID.fromString(rs.getString("id")), // Mivel UUID az ID
                rs.getString("address"),
                rs.getString("location"),
                rs.getDate("date").toLocalDate(),
                rs.getBoolean("is_online")
        );
    };

    public JdbcCalendarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Calendar> findAll() {
        String sql = "SELECT * FROM calendar";
        log.info("Fetching all calendars from database");
        List<Calendar> calendars = jdbcTemplate.query(sql, calendarRowMapper);
        log.info("Successfully fetched {} calendars", calendars.size());
        return calendars;
    }

    @Override
    public Calendar create(Calendar calendar) {
        String sql = "INSERT INTO calendar (id, address, location, date, is_online) VALUES (?, ?, ?, ?, ?)";
        log.info("Creating calendar with ID: {}", calendar.getId());
        jdbcTemplate.update(sql,
                calendar.getId().toString(),
                calendar.getAddress(),
                calendar.getLocation(),
                java.sql.Date.valueOf(calendar.getDate()),
                calendar.getOnline()
        );
        log.info("Successfully created calendar with ID: {}", calendar.getId());
        return calendar;
    }

    @Override
    public void update(Calendar calendar) throws RepositoryException {
        String sql = "UPDATE calendar SET address = ?, location = ?, date = ?, is_online = ? WHERE id = ?";
        log.info("Updating calendar with ID: {}", calendar.getId());
        int rowsAffected = jdbcTemplate.update(sql,
                calendar.getAddress(),
                calendar.getLocation(),
                java.sql.Date.valueOf(calendar.getDate()),
                calendar.getOnline(),
                calendar.getId().toString()
        );
        if (rowsAffected == 0) {
            log.warn("No calendar found with ID: {}", calendar.getId());
            throw new RepositoryException("No calendar found with ID: " + calendar.getId());
        }
    }

    @Override
    public void deleteById(UUID id) throws RepositoryException {
        String sql = "DELETE FROM calendar WHERE id = ?";
        log.info("Deleting calendar with ID: {}", id);
        int rowsAffected = jdbcTemplate.update(sql, id.toString());
        if (rowsAffected == 0) {
            log.warn("No calendar found with ID: {}", id);
            throw new RepositoryException("No calendar found with ID: " + id);
        }
    }

    @Override
    public Optional<Calendar> findById(UUID id) {
        String sql = "SELECT * FROM calendar WHERE id = ?";
        log.info("Fetching calendar with ID: {}", id);
        List<Calendar> calendars = jdbcTemplate.query(sql, calendarRowMapper, id.toString());
        if (calendars.isEmpty()) {
            log.warn("No calendar found with ID: {}", id);
            return Optional.empty();
        } else {
            log.info("Successfully fetched calendar with ID: {}", id);
            return Optional.of(calendars.getFirst());
        }
    }
}
